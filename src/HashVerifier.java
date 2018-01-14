/*
 * Basic program for future USERs to:
 *--Verify the Final OutPut Hash of their ID and Total Rolls
 *--Loads TXIDList from ./src/TXIDLISTOne.txt
 *--Displays Unique Secure Hash using the 2 USER Inputs and their TXID
 *--Handles FileNotFound and IOException.
 */
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HashVerifier {

    private static Scanner scanner = new Scanner( System.in );

	
	public static void main(String[] args) throws IOException {

		try {
			System.out.println("~~~~This Program Allows You to Verify the FINAL Hash of Your Game~~~~");
			System.out.println("~~~~Enter Your ID and The Total of your Rolls when prompted below~~~~");
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks USER for first input
		System.out.println("\nEnter Your ID: ");
		String input1 = String.valueOf(scanner.nextLine());

		try {
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks USER for second input
		System.out.println("\nEnter the Total of your Rolls: ");
		String input2 = String.valueOf(scanner.nextLine());

		try {
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}


	    HashMap<Integer, String> TXIDList = new HashMap<Integer, String>();

		BufferedReader TXID = new BufferedReader( new FileReader ("./src/TXIDListOne.txt"));
		String line = ":";

	    int i = 1; //start from i = 1 instead of i = 0 otherwise output starts at 0 is:... instead of 1 is:...
	    
	    while ((line = TXID.readLine()) != null) {
	    	
	        TXIDList.put(i, line);
	        i++;
	    }
	    TXID.close();

		if (TXIDList.size() == 1 && TXIDList.size() != 0) {

			for (int item : TXIDList.keySet()) {
				System.out.println("\nThe given TXID Hash is: " + TXIDList.get(item) + "." + "\n");
			}
		}
		else {

			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
		}

		for (int k=0; k<TXIDList.size(); k++)
		{
			//1. String _idtoString = _id;
			String _id = input1;
			//2. String _totaltoString = _total;
			String _total = input2;
			//3. append id before total
			String _id_total = (_id+_total);
			//4. append id before idtotal before total
			String unQString = (_id+_id_total+_total);

			//5. idtoSHA256: sha256Hex of id
			String _idtoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id);
			//6. totaltoSha256: sha256Hex of total
			String _totaltoSHA512 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(_total);
			//7. stoHash1: appended _id_total's Sha256Hex
			String stoHash1 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id_total);

			//8. append id before stoHash1(appended _id_total's Sha256Hex) before total
			String hString1 = (_id+stoHash1+_total);
			//9. append idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
			String hString2 = (_idtoSHA256+unQString+_totaltoSHA512);
			//10. sha256hex of hstring1: appended id before stoHash1(appended _id_total's Sha256Hex) before total
			String hS1toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1);
			//11. sha256 of hstring2: appended idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
			String hS2toSHA512 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hString2);
			//12. sha256hex of hString1 appended before hString2
			String hStoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1+hString2); 

			//13: 
			String hString3 = (hS1toSHA256+hS2toSHA512+hStoSHA256);
			//Final0: 
			String hashFinal = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hString3);

			System.out.println("~~~~Commencing HASH Verification Display:~~~~");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			System.out.println("Your ID as you entered: "+_id+"." + "\nTotal of your Rolls as you entered: " + _total+".\n");

			try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDListOne.txt"))) {
				   String _line = null;
				   if ((_line = show.readLine()) != null) {
					   String hashZ1 = (hashFinal+_line);
					   String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hashZ1);
					   String hashZ3 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hashZ2);
					   String hashZf = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ3);
					   String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZf);
					   System.out.println("Your FINAL OUTPUT HASH is:----> "+hashZFinal+".\n");
					   System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
				       }
				   } catch (FileNotFoundException e) {
						throw new RuntimeException("Error! File not found! Make sure the filepath is correct!");
				} catch (IOException e) {
					throw new RuntimeException("Error! Something went wrong! Please try again!");
				}
			System.out.println("\n~~~END HASHING, ENJOY!~~~\n");
		}
	}
}