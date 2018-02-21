
/*
 * Basic program.
 *--Verify the Hash:
 *--Loads and Checks if there is Master HashKey in ./src/TXIDListOne.txt
 *--Generates Random Hash from current Date and Time or
 *--Displays Unique Hash using the 2 USER Inputs
 *--Saves a Screenshot to ./out/ss/<HASH>.gif or
 *--If image exists, saves a Screenshot to ./out/ss/checked/<HASH>.gif
 *--Handles Exception errors.
 */
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 * @author per_z
 *
 */
public class HashVerifier {

	int salt;

	public HashVerifier() {
		salt = 0;
	}

	final int Salt() throws NoSuchAlgorithmException {
		// Create Salt using SecureRandom SHA1PRNG
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		salt = random.nextInt(9000000) + 1000000;
		return salt;
	}

	protected static String sha256(String args) {
		// Hash using SHA256
		String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(args);
		return hash;
	}

	protected static String sha512(String args) {
		// Hash using SHA512
		String hash = org.apache.commons.codec.digest.DigestUtils.sha512Hex(args);
		return hash;
	}

	private static Object dated() {
		// Instantiate a Date object
		Date date = new Date();
		return date;
	}

	private static String dates() {
		// Create variable for current date
		String dDate = String.format("%tm%<td%<tY", HashVerifier.dated());
		return dDate;
	}

	private static String times() {
		// Create variable for instanced time
		String dTime = String.format("%tH%<tM%<tS", HashVerifier.dated());
		return dTime;
	}

	private static FileReader fR() throws FileNotFoundException {
		// Load Master HashKey from file
		return new FileReader("./src/TXIDListOne.txt");
	}

	private static Rectangle Capture() throws AWTException {
		// Determine current screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle screenRect = new Rectangle(screenSize);
		return screenRect;
	}

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		System.out.println("~~~~This Program Generates & Verifies the HASH.");
		System.out.println("~~~~Enter The Date and The Time below if not Auto Generate.");
		System.out.println("~~~~Example Date: mmddyyyy like 02062018 for 02.06.2018");
		System.out.println("~~~~Example Time: hhmmss like 120101 for 12:01:01 PM");
		// display date and time using toString()
		System.out.println("\nCurrent Date and Time: " + dated().toString());
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

		boolean dicer;

		// Set dicer to true or false
		System.out.println("\nAuto Generate? Type 'true' or 'false'.");
		String input = String.valueOf(scanner.nextLine());
		String input1 = null;
		String input2 = null;
		dicer = Boolean.valueOf(input);

		try {
			// ask for date and time if dicer is false
			if (dicer != true) {
				// Asks USER for Date
				System.out.println("\nEnter the Date: ");
				input1 = String.valueOf(scanner.nextLine());
				TimeUnit.SECONDS.sleep(1); // wait 1 second
				// Asks USER for Time
				System.out.println("\nEnter the Time: ");
				input2 = String.valueOf(scanner.nextLine());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
		}

		// Asks USER for third input
		System.out.println("\nEnter the Hash to verify: ");
		String input3 = String.valueOf(scanner.nextLine());

		HashMap<Integer, String> TXIDList = new HashMap<Integer, String>();

		BufferedReader TXID = new BufferedReader(fR());
		String line = ":";

		int i = 1; // start from i = 1 instead of i = 0
		while ((line = TXID.readLine()) != null) {
			TXIDList.put(i, line);
			i++;
		}
		TXID.close();

		if (TXIDList.size() != 1) {
			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
		} else if (TXIDList.size() == 1) {

			String uDate = null;
			String uTime = null;
			String uDT = null;
			String unQ = null;

			// if dicer is true, use salt method for Random Hash Generation
			// if dicer is false, use append method for Fixed Hash Generation
			if (dicer != false) {
				// 3a. use SecureRandom to create salt
				HashVerifier mySalt = new HashVerifier();
				int dSalt = mySalt.Salt();

				// 3b. append id before salt before total
				uDate = (dates());
				uTime = (times());
				uDT = (uDate + uTime);
				unQ = (uDate + String.valueOf(dSalt) + uTime);
				System.out.println("\nSalt: " + dSalt + ".");
				System.out.println("Unique String: " + unQ + ".\n");
			} else {
				uDate = input1;
				uTime = input2;
				uDT = (uDate + uTime);
				// 3c. append id before uDateuTime before total
				unQ = (uDate + uDT + uTime);
				System.out.println("\nUnique String: " + unQ + ".\n");
			}
			// 4. 3b or 3c
			String unQString = (unQ);

			// 5. idtoSHA256: sha256Hex of id
			String uDatetoSHA256 = sha256(uDate);
			// 6. totaltoSha256: sha256Hex of total
			String uTimetoSHA512 = sha512(uTime);
			// 7. stoHash1: appended uDateuTime's Sha256Hex
			String stoHash1 = sha256(uDate + uTime);

			// 8. append id before stoHash1(appended uDateuTime's Sha256Hex) before total
			String hString1 = (uDate + stoHash1 + uTime);
			// 9. append idtoSHA256 before unQString before totaltoSHA256
			String hString2 = (uDatetoSHA256 + unQString + uTimetoSHA512);
			// 10. sha256hex of hstring1: appended id before stoHash1(appended uDateuTime's
			// Sha256Hex) before total
			String hS1toSHA256 = sha256(hString1);
			// 11. sha256 of hstring2: appended idtoSHA256 before unQString before
			// totaltoSHA256
			String hS2toSHA512 = sha512(hString2);
			// 12. sha256hex of hString1 appended before hString2
			String hStoSHA256 = sha256(hString1 + hString2);

			// 13:
			String hString3 = (hS1toSHA256 + hS2toSHA512 + hStoSHA256);
			// Final0:
			String hashFinal = sha512(hString3);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("\n~~~~Commencing HASH Verification Display\n");
			System.out.println("\nThe Date: " + uDate + "." + "\nThe Time: " + uTime + ".\n");
			System.out.println("Organization: PZ.");

			try (BufferedReader show = new BufferedReader(fR())) {
				String _line = null;
				if ((_line = show.readLine()) != null) {
					String hashZ1 = (hashFinal + _line);
					String hashZ2 = sha512(hashZ1);
					String hashZ3 = sha512(hashZ2);
					String hashZf = sha256(hashZ3);
					String hashZFinal = sha256(hashZf);

					if (input3.equals(hashZFinal)) {
						System.out.println("\nThe HASH is correct.");
						System.out.println("\nThe Hash for that Date and Time: " + hashZFinal + ".");
					} else {
						System.out.println("\nThe HASH is incorrect! Make sure you entered correctly!");
						// remove next line for production
						System.out.println("\nThe Hash for that Date and Time: " + hashZFinal + ".");
					}

					// Screen Capture section
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
					System.out.println("~~~~Screen Capture\n");
					String outImage = ("./out/ss/[" + uDate + "_" + uTime + "] " + hashZFinal + ".gif");
					String outImageNew = ("./out/ss/checked/[" + dates() + "_" + times() + "] " + hashZFinal
							+ "_checked.gif");
					File f = new File(outImage);
					File fNew = new File(outImageNew);
					try {
						Robot robot;
						robot = new Robot();
						BufferedImage image = robot.createScreenCapture(Capture());

						if (f.exists() && !f.isDirectory() && fNew.exists() && !fNew.isDirectory()) {
							System.out.println("\nThe image exists in both folders! No screenshots taken!");
						} else if (f.exists() && !f.isDirectory()) {
							System.out.println("\nThe image exists, now taking a checked screenshot!");
							// save captured image to GIF file
							ImageIO.write(image, "gif", fNew);
							// give feedback
							System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight()
									+ ") to file: " + fNew + ".");
						} else {
							// save captured image to GIF file
							ImageIO.write(image, "gif", f);
							// give feedback
							System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight()
									+ ") to file: " + f + ".");
						}
					} catch (AWTException e1) {
						throw new RuntimeException("Error! File not found! Make sure the folder exists!");
					}
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error! File not found! Make sure the filepath is correct!");
			} catch (IOException e) {
				throw new RuntimeException("Error! Something went wrong! Please try again!");
			}
		}
	}
}