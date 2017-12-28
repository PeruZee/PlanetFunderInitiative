/*Initiator: Pruz 12/2017
 *
 *
 */

import java.util.Random;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TXIDForEach {

	public static int rollDice(int number, int nSides) {

		int num = 0;
		int roll = 0;
		Random  r = new Random();
		if (nSides >=6) {

			for (int i = 0; i < number; i++) {

				roll = r.nextInt(nSides)+1;
				System.out.println("Roll is: "+ roll);
				num = num + roll;
			}
		}
		else {

			System.out.println("Error: The number picked needs to be from 6");
		}
		return num;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		HashMap<Integer, String> TXIDList = new HashMap<Integer, String>();

		BufferedReader TXID = new BufferedReader( new FileReader ("./src/TXIDList.txt"));
		String line = ":";

		int i = 1; //start from i = 1 instead of i = 0 otherwise output starts at 0 is:... instead of 1 is:...

		while ((line = TXID.readLine()) != null) {

			TXIDList.put(i, line);
			i++;
		}
		TXID.close();

		if (TXIDList.size() > 1 && TXIDList.size() <= TXIDList.size()) {

			System.out.println("\n" + "There are a total of: " + TXIDList.size() + " TXIDs." + "\n");
			for (int item : TXIDList.keySet()) {

				System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
				System.out.println("\n" + "Total of all rolls for " + TXIDList.get(item) + " is: "+ rollDice(TXIDList.size() * 1, TXIDList.size()) + "\n");
			}
		}
		else if (TXIDList.size() == 1) {

			System.out.println("\n" + "There is: " + TXIDList.size() + " TXID." + "\n");
			for (int item : TXIDList.keySet()) {

				System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
				System.out.println("\n" + "Total of all rolls for " + TXIDList.get(item) + " is: "+ rollDice(TXIDList.size() * 7, 6) + "\n");
			}
		}
		else {			

			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
		}
	}
}
