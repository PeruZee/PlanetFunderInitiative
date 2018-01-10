/*
 * Original: Pruz (10.01.2018)
 * This programs prompts USER for string=_id and string=_total 
 * and also loads TXIDList from ./src/TXIDLISTOne.txt
 * Builds an Unique Secure Hash using the 2 USER Inputs and their TXID
 * Handles ioexception
 */
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HashBuilder {

    private static Scanner scanner = new Scanner( System.in );

	
	public static void main(String[] args) throws IOException {

		// Asks USER for first input
		System.out.println("\nEnter the first input: ");
		String input1 = String.valueOf(scanner.nextLine());

		try {
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks USER for second input
		System.out.println("\nEnter the second input: ");
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
		//resultList = new ArrayList<int[]>();	    

		if (TXIDList.size() > 1) {
			
			System.out.println("\n" + "There are a total of: " + TXIDList.size() + " TXIDs." + "\n");
			for (int item : TXIDList.keySet()) {
				
				System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
			}
		}
		else if (TXIDList.size() == 1) {
			
			System.out.println("\n" + "There is: " + TXIDList.size() + " TXID." + "\n");
			for (int item : TXIDList.keySet()) {
				
				System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
			}
		}
		else {			

			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
		}
		
		for (int k=0; k<TXIDList.size(); k++)
		{
			String _id = input1; //String _idtoString = _id;
			String _total = input2; //String _totaltoString = _total;

			//append id before total
			String _id_total = (_id+_total);
			
			//append id before idtotal before total
			String unQString = (_id+_id_total+_total);
			
			//idtoSHA256: sha256Hex of id
			String _idtoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id);
			
			//totaltoSha256: sha256Hex of total
			String _totaltoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_total);
			
			//stoHash1: appended _id_total's Sha256Hex
			String stoHash1 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id_total);

/*
 * future complexity implementation
 * 			String stoHash2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(unQString);
 *
 */			
			//append id before stoHash1(appended _id_total's Sha256Hex) before total
			String hString1 = (_id+stoHash1+_total);
			//append idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
			String hString2 = (_idtoSHA256+unQString+_totaltoSHA256);
/*			
 * future complexity implementation
 * 		String hStringZ = (hString1+stoHash2+hString2);
 */

			//sha256hex of hstring1: appended id before stoHash1(appended _id_total's Sha256Hex) before total
			String hS1toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1);
			
			//sha256 of hstring2: appended idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
			String hS2toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString2); 
			
			//sha256hex of hString1 appended before hString2
			String hStoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1+hString2); 
			
/*
//future complexity implementation
 * 		String dblh1SHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hS1toSHA256);
 * 		String dblh2SHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hS2toSHA256);
 * 		String hString3 = (dblh1SHA256+dblh2SHA256);
 *  //future complexity implementation
 */			
			String hString3 = (hS1toSHA256+hS2toSHA256);
			
			String hashFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString3);
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			
			System.out.println("Your First Input: ("+_id+", " + "Your Second Input: " + _total+".)");

			System.out.println("~~~Commencing HASHING:~~~");
			//append id before total
			System.out.println("\nFirst Unique String: "+_id_total+".");
			//append id before idtotal before total
			System.out.println("Second Unique String: "+unQString+".");
			
			System.out.println("\nFirst Unique HashString: "+hString1+".");
			System.out.println("Second Unique HashString: "+hString2+".");
			
			System.out.println("\nhS1toSHA256: "+hS1toSHA256+".");
			System.out.println("hS2toSHA256: "+hS2toSHA256+".");
			System.out.println("hStoSHA256: "+hStoSHA256+".\nhString1: "+(hString1+"\nhString2: "+hString2));
			
			System.out.println("\nThird Unique HashString: "+hString3+".");
			
			System.out.println("\nFinal Hash before final append: "+hashFinal+".\n");
			
			
			try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDListOne.txt"))) {
				   String _line = null;
				   if ((_line = show.readLine()) != null) {				   
					   String hashZ1 = (_line+hashFinal);
					   String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ1);
					   String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ2);
					   System.out.println("\nFinal String after final append with TXID (hashZ1): "+hashZ1+".\n");
					   System.out.println("hashZ2: "+_line+"'s Final String's First Unique HASH is----> "+hashZ2+"\n");
					   System.out.println("\n\nTXID: "+_line+"'s Second  and Final Unique HASH is----> "+hashZFinal+"\n");
					   System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
				       }
				   } catch (FileNotFoundException e) {
						throw new RuntimeException("Error! File not found! Make sure there the filepath is correct!");
				} catch (IOException e) {
					throw new RuntimeException("Error! Something went wrong! Please try again!");
				}
			System.out.println("\n~~~END HASHING, ENJOY!~~~\n");
		}
	}
}
