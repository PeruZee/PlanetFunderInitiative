package lottoTools;
public final class Player 
{
	//Variables
	private int total=0, id;
	private String TXID, hash;
	private int[] rolls;
	
	//Methods
	public Player() {
		
	}
	public Player(String _TXID, int _id, int[] _rolls) {
		TXID = _TXID;
		id = _id;
		rolls = _rolls;
		getTotal();
	}
	
	public void setID(int _id) {
		id=_id;
	}
	public void setTXID(String _txid) {
		TXID = _txid;
	}
	public void setHash(String _hash) {
		hash=_hash;
	}
	
	public int getTotal() {
		if(total ==0) 
		total= _getTotalFromRolls();
		
		return total;
	}
	
	public int getID() {
		return id;
	}
	
	public String getTXID() {
		return TXID;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setRolls(int[] _rolls) {
		rolls =_rolls;
	}
	public int[] getRolls() {
		return rolls;
	}
	
	private int _getTotalFromRolls() {
		int _total=0;
		for(int i=0; i<rolls.length; i++) {
			_total += rolls[i];
		}
		return _total;
	}
}
