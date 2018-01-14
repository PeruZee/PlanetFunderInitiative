package lottoTools;
import lottoTools.helpers.*;
import java.util.ArrayList;
public final class Display implements IObserver
{
	static boolean initialized=false;
	static Display instance;
	public Display() {
	if(!initialized)
	instance = this;
	initialized=true;
	}
	
	public void onNotify(Object obj, String args) {
		if(obj instanceof Session) {
		}
	}
	
	public void onNotify(Object obj, String args, Object param) 
	{
		if(obj instanceof Session) 
		{
			if(args == "dispalyTXIDNum") 
			{
				int nSize= (int) param;
				_displayTXIDNum(nSize);
			}
			else if (args =="showPlaerInfo") {
				ArrayList<Player> players = (ArrayList<Player>) param;
				_showPlayersInfo(players);
			}
			else if(args == "showFinalResults") {
				ArrayList<Player> players = (ArrayList<Player>) param;
				_showFinalResults(players);
			}
		}
	}
	
	private void _displayTXIDNum(int nSize) {
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
	}
		
	}
	
	private void _showPlayersInfo(ArrayList<Player> players)
	{
		//resultList.add(new int[]{item, rollTotal});
		//String sha256hex = Lotto.Hash.sha256Hex(Double.toString(rollTotal));
		//System.out.println("\n"+"~~~Total of all rolls for "+ TXIDList.get(item)+"is: "+rollTotal);
		//System.out.println("~~~HASH for Total Roll of "+rollTotal+": "+ sha256hex+"\n");
		for(Player player : players) 
		{
			
			int rollTotal = player.getTotal();
			int id = player.getID();
			String TXID = player.getTXID();
			String hash = player.getHash();
			int[] rolls = player.getRolls();
			
			System.out.println(id + " is: " + TXID + "." + "\n");
			for(int i=0; i<rolls.length;i++)
			{
				System.out.println("Roll is: "+ rolls[i]);
			}
			String sha256hex = Lotto.Hash.sha256Hex(Double.toString(rollTotal));
			System.out.println("\n"+"~~~Total of all rolls for "+ id+" is: "+rollTotal);
			System.out.println("~~~HASH for Total Roll of "+rollTotal+": "+ sha256hex);
			System.out.println("~~~FINAL >>SHA256Hex<< HASH is:----> "+hash+".\n");
			
			
		}
	}
	
	private void _showFinalResults(ArrayList<Player> players) 
	{
		System.out.println();
		int n= players.size();
		int position=1;
		String line="";
		System.out.println("~~~Showing Placement(Place: ID):\n");
		for(int i=0; i<n; i++)
		{
			line += Generic.Ordinal(position)+ " place: ";
			Player _this = players.get(i);
			line+= Integer.toString(_this.getID());
			int j=i+1;
			while(j!=n)
			{
				Player _next = players.get(j);
				if(_next.getTotal() == _this.getTotal())
					line+=", " + Integer.toString(_next.getID());
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
	
}
