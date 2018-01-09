/*Initiator: Pruz 20/12/2017
 *First Contributor: mody1710 28/12/2017
 *
 */

import java.util.Random;
import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		Random  r = new Random();
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
				String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(Integer.toString(rollTotal));
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
			String _total = Integer.toString(resultList.get(i)[1]);
			String _id = Integer.toString(resultList.get(i)[0]);

			String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_total);
			System.out.println("ID: " + _id +", " + "Total: " + _total + " (Hash: " + sha256hex+")");
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

		//Show the list of participants and HASH to SHA256
		System.out.println("\n~~~Showing list of participants:\n");
		try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDList.txt"))) {
			   String line = null;
			   while ((line = show.readLine()) != null) {
				   String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(line);
			       System.out.println(line+" <--is--> "+sha256hex);
			       }
			   }
		}
	}