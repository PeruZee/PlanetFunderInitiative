package lottoTools.helpers;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;

public final class Lotto {
	
	public static final lottoTools.secret.Hash Hash; 
	 /**
	 * LoadTXIDList: Loads the list of TXIDs stored in a specific path and returns a HashMap
	 * @param _filename: Path and name of file containing the TXID list
	 * @return A HashMap representing data 
	 */
	public static  HashMap<Integer, String> LoadTXIDList (String _filename) 
			throws FileNotFoundException, IOException 
	{
		//Hash.Something();
		HashMap<Integer, String> _idList;
		BufferedReader _buffer;
		_idList = new HashMap<Integer, String>();
		_buffer = new BufferedReader( new FileReader (_filename));
		String line = ":";

		int i = 1; //start from i = 1 instead of i = 0 otherwise output starts at 0 is:... instead of 1 is:...

		while ((line = _buffer.readLine()) != null) {

			_idList.put(i, line);
			i++;
		}
		_buffer.close();
		return _idList;
	}
	//rollDice: rolls the dice, outputs the result of each and returns the total
	public static int[] rollDice(int number, int nSides) {

		int roll = 0;
		int[] rolls= new int[number];
		Random  r = new SecureRandom();
		if (nSides >=10) {

			for (int i = 0; i < number; i++) {

				roll = r.nextInt(nSides)+1;
				rolls[i] = roll;
			}
		}
		else {

			System.out.println("Error: The number picked needs to be from 10");
		}
		return rolls;
	}
	
	static {
		Hash = lottoTools.secret.Hash.get();
	}

}
