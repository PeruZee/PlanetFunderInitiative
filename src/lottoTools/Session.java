package lottoTools;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jdk.management.resource.NotifyingMeter;
import lottoTools.helpers.*;
import lottoTools.secret.Hash;
public final class Session implements ISubject
{
	private static boolean initialized = false;
	private static HashMap<Integer, String> TXIDList;
	private static ArrayList<Player> players;
	
	private Display display;
	
	public Session(Display _display) 
	{
		if(!initialized)
		{
			display = _display;
			display.addSubject(this);
			Lotto.Hash.addSubject(this);
		}
	}
	public void start() throws FileNotFoundException, IOException {
		if(!initialized)
		{
			init();
			initialized=true;
			
		}
	}
	private void init() throws FileNotFoundException, IOException {
		players = new ArrayList<Player>();
		TXIDList = Lotto.LoadTXIDList("./src/TXIDList.txt");
		performThrows();
		announceResults();
	
	}
	private int performThrows() {
		int nSize= TXIDList.size();
		Notify(display, "dispalyTXIDNum", (Object)nSize);
		if (nSize >= 10) {
			//Variables that change based on number of IDs 
			int rollNumber =nSize;
			int rollSides = nSize;

			//Change previous variables if there's less than 10 ID
			if(nSize==1 || nSize<=9 ) {
				rollNumber =nSize * 10;
				rollSides = 10;
			}
			for (int item : TXIDList.keySet()) {
				//System.out.println(item + " is: " + TXIDList.get(item) + "." + "\n");
				int[] _rolls = Lotto.rollDice(rollNumber, rollSides);
				players.add(new Player(TXIDList.get(item), item, _rolls));
//tbc
				//resultList.add(new int[]{item, rollTotal});
				//String sha256hex = Lotto.Hash.sha256Hex(Double.toString(rollTotal));
				//System.out.println("\n"+"~~~Total of all rolls for "+ TXIDList.get(item)+"is: "+rollTotal);
				//System.out.println("~~~HASH for Total Roll of "+rollTotal+": "+ sha256hex+"\n");
			}
			Notify(Lotto.Hash, "requestHash");
			Hash.requestPlayerHashGeneration(players);
			Notify(display, "showPlaerInfo", players);
		}

		else {			
			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
			return 1; //Indicating an error
		}

		return 0;
	}
	
	private void announceResults() {
		lottoTools.helpers.Generic.SortList(players, false, Generic.IListTools.IListGetPlayerTotal);
		Notify(display, "showFinalResults", players);
	}

	
}
