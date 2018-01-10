/*Initiator: Pruz 20/12/2017
 *First Contributor: mody1710 28/12/2017
 *
 */

import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;

public class TXIDForEach {
	//============Objects============//

	private static HashMap<Integer, String> TXIDList;
	private static BufferedReader TXID;
	private static ArrayList<int[]> resultList; //int[] {ItemID, Total}

	//============Methods============//

	//rollDice: rolls the dice, outputs the result of each and returns the total
	private static int rollDice(int number, int nSides) {

		int num = 0;
		int roll = 0;
		Random  r = new SecureRandom();
		if (nSides >=10) {

			for (int i = 0; i < number; i++) {

				roll = r.nextInt(nSides)+1;
				System.out.println("Roll is: "+ roll);
				num = num + roll;
			}
		}
		else {

			System.out.println("Error: The number picked needs to be from 10");
		}
		return num;
	}

	//init: initializes the program and any necessary objects
		private static void init() throws FileNotFoundException, IOException   {
			resultList = new ArrayList<int[]>();
			loadList("./src/TXIDList.txt");
		}

	//loadLists: Loads the list of TXIDs stored in a specific filename
	private static void loadList(String _filename) throws FileNotFoundException, IOException {
		TXIDList = new HashMap<Integer, String>();
		TXID = new BufferedReader( new FileReader (_filename));
		String line = ":";

		int i = 1; //start from i = 1 instead of i = 0 otherwise output starts at 0 is:... instead of 1 is:...

		while ((line = TXID.readLine()) != null) {

			TXIDList.put(i, line);
			i++;
		}
		TXID.close();
	}

	//performThrows: performs the actual throws and stores IDs and totals in resultList and HASH with SHA256
	private static int performThrows() {

		int nSize= TXIDList.size();
		if (nSize >= 10) {
			//Variables that change based on number of IDs 
			String quantityLine = "There are a total of: ";  
			String unitLine =" TXIDs.";
			int rollNumber =nSize;
			int rollSides = nSize;

			//Change previous variables if there's less than 10 ID
			if(nSize==1 || nSize<=9 ) {
				quantityLine = "There is: ";  
				unitLine =" TXID.";
				rollNumber =nSize * 10;
				rollSides = 10;
			}

			System.out.println("\n" + quantityLine + nSize + unitLine + "\n");
			for (int item : TXIDList.keySet()) {
				System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
				int rollTotal = rollDice(rollNumber, rollSides);
				
//tbc
				String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(Double.toString(rollTotal));
				System.out.println("\n"+"~~~Total of all rolls for "+ TXIDList.get(item)+"is: "+rollTotal);
				System.out.println("~~~HASH for Total Roll of "+rollTotal+": "+ sha256hex+"\n");
				resultList.add(new int[]{item, rollTotal});
			}
		}

		else {			
			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
			return 1; //Indicating an error
		}

		return 0;
	}

	//sortResultArray: sorts result array with the bubble sort algorithm and HASH with SHA256
	private static void sortResultArray() {
		//sort with the bubble sort algorithm
		int n=resultList.size();
		for(int i=0; i<n-1; i++)
		{
			for(int j=0; j<n-i-1;j++)
			{
				int _this =resultList.get(j)[1];
				int _next =resultList.get(j+1)[1];
				if(_this<_next)
				{
					Collections.swap(resultList, j, j+1);
				}
			}
		}

		for (int i=0; i<resultList.size(); i++)
		{
			String _id = Integer.toString(resultList.get(i)[0]); //String _idtoString = _id;
			String _total = Integer.toString(resultList.get(i)[1]); //String _totaltoString = _total;

			String _id_total = (_id+_total);
			String unQString = (_id+_id_total+_total);
			

			String _idtoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id);
			String _totaltoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_total);
			String stoHash1 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id_total);

//			String stoHash2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(unQString); //future complexity implementation
			
			String hString1 = (_id+stoHash1+_total);
			String hString2 = (_idtoSHA256+unQString+_totaltoSHA256);
//			String hStringZ = (hString1+stoHash2+hString2); //future complexity implementation
			
			String hS1toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1); //use this
			String hS2toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString2); //use this no 3
			String hStoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1+hString2); //use this no 4
			
//			String dblh1SHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hS1toSHA256); //future complexity implementation
//			String dblh2SHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hS2toSHA256); //future complexity implementation
//			String hString3 = (dblh1SHA256+dblh2SHA256); //future complexity implementation
			
			String hString3 = (hS1toSHA256+hS2toSHA256);
			
			String hashFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString3);
			
			System.out.println("ID: \n("+_id+", " + "Total: " + _total+".)");

			System.out.println("~~~Commencing HASHING:~~~");
			System.out.println("\nFirst Unique String: "+_id_total+".");
			System.out.println("Second Unique String: "+unQString+".");
			
			System.out.println("\nFirst Unique HashString: "+hString1+".");
			System.out.println("Second Unique HashString: "+hString2+".");
			
			System.out.println("\nhS1toSHA256: "+hS1toSHA256+".");
			System.out.println("hS2toSHA256: "+hS2toSHA256+".");
			System.out.println("hStoSHA256: "+hStoSHA256+".");
			
			System.out.println("\nThird Unique HashString: "+hString3+".");
			
			System.out.println("\nFinal Hash before final append: "+hashFinal+".");
			
			try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDList.txt"))) {
				   String line = null;
				   while ((line = show.readLine()) != null) {				   
					   String hashZ1 = (line+hashFinal);
					   String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ1);
					   String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ2);
				       System.out.println(line+"'s Unique HASH is----> "+hashZFinal);
				       }
				   } catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("\n~~~END HASHING, ENJOY!~~~\n");
/*
 * 			String hashZ1 = (line+hashFinal);
 *			String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ1);
 *			String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ2);
 *		    System.out.println(line+" <--is--> "+hashZFinal);
 *
 */
		}
	}

	//ordinal: returns the String ordinal of a number
	private static String ordinal(int i) {
	    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + sufixes[i % 10];
	    }
	}

	//showResults: shows score order and winners
	private static void showResults() {
		sortResultArray();
		System.out.println();

		int n= resultList.size();
		int position=1;
		String line="";
		System.out.println("~~~Showing Placement(Place: ID):\n");
		for(int i=0; i<n; i++)
		{
			line += ordinal(position)+ " place: ";
			int[] _this = resultList.get(i);
			line+= Integer.toString(_this[0]);
			int j=i+1;
			while(j!=n)
			{
				int[] _next = resultList.get(j);
				if(_next[1] == _this[1])
					line+=", " + Integer.toString(_next[0]);
				else 
					break;
				j++;
			}

			i= j-1;
			System.out.println(line);
			line="";
			position++;
		}
		
	}

	//============Main============// 
	public static void main(String[] args) throws FileNotFoundException, IOException {

		init(); //initializes the program and any necessary objects
		if(performThrows() ==1 ) System.exit(1); //perform the throws and if no IDs found, exit
		System.out.println("\n~~~Showing Results:\n");
		showResults();

/*		//Show the list of participants and HASH to SHA256
		System.out.println("\n~~~Showing list of participants:\n");
		try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDList.txt"))) {
			   String line = null;
			   while ((line = show.readLine()) != null) {				   
				   String hashZ1 = (line+hashFinal);
				   String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ1);
				   String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ2);
			       System.out.println(line+" <--is--> "+hashZFinal);
*/
			       }
		}