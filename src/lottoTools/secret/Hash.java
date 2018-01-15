package lottoTools.secret;

import java.util.ArrayList;

import lottoTools.Player;
import lottoTools.Session;
import lottoTools.helpers.*;

public final class Hash implements IObserver{
	private static boolean shouldGeneratePlayerHash = false;
	private static Hash instance=null;
	protected Hash() {}
	public static Hash get() {
		
		if(instance==null) {
			instance = new Hash();
			return instance;
		}
		return null;
	}
	public static String sha256Hex(String _str) {
		String _hash =org.apache.commons.codec.digest.DigestUtils.sha256Hex(_str);
		return _hash;
	}
	public static String sha512Hex(String _str) {
		String _hash =org.apache.commons.codec.digest.DigestUtils.sha512Hex(_str);
		return _hash;
	}
	public static void requestPlayerHashGeneration(ArrayList<Player> _players) 
	{
		if(shouldGeneratePlayerHash) 
			instance._generatePlayerHashes(_players);
			shouldGeneratePlayerHash =false;
	}
	private void _generatePlayerHashes(ArrayList<Player> _players) {
		for(Player pl : _players) {
			String _hash = _generateHash(pl);
			pl.setHash(_hash);
		}
	}
	private String _generateHash(Player _player) {
		int _id = _player.getID();
		int _total = _player.getTotal();
		String _txid = _player.getTXID();
		return _generateHash(_id, _total, _txid);
	}
	private String _generateHash(int nId, int nTotal, String nTXID) 
	{

		String _id = Integer.toString(nId); //String _idtoString = _id;
		String _total = Integer.toString(nTotal); //String _totaltoString = _total;
		
		//3. append id before total
		String _id_total = (_id+_total);
		
		//4. append id before idtotal before total
		String unQString = (_id+_id_total+_total);
		
		//5. idtoSHA256: sha256Hex of id
		String _idtoSHA256 = sha256Hex(_id);
		
		//6. totaltoSha256: sha256Hex of total
		String _totaltoSHA512 = sha512Hex(_total);
		
		//7. stoHash1: appended _id_total's Sha256Hex
		String stoHash1 = sha256Hex(_id_total);
		
		
		//8. append id before stoHash1(appended _id_total's Sha256Hex) before total
		String hString1 = (_id+stoHash1+_total);
		//9. append idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
		String hString2 = (_idtoSHA256+unQString+_totaltoSHA512);
		
		
		//10. sha256hex of hstring1: appended id before stoHash1(appended _id_total's Sha256Hex) before total
		String hS1toSHA256 = sha256Hex(hString1);
		
		//11. sha256 of hstring2: appended idtoSHA256 before unQString(appended id before idtotal before total) before totaltoSHA256
		String hS2toSHA512 = sha512Hex(hString2); 
		
		//12. sha256hex of hString1 appended before hString2
		String hStoSHA256 = sha256Hex(hString1+hString2); 
				
		//13: 
		String hString3 = (hS1toSHA256+hS2toSHA512+hStoSHA256);			
		
		//Final0. 
		String hashFinal = sha512Hex(hString3);
		
		String hashZ1 = (hashFinal+nTXID);
		String hashZ2 = sha512Hex(hashZ1);
		String hashZ3 = sha512Hex(hashZ2);
		String hashZf = sha256Hex(hashZ3);
		String hashZFinal = sha256Hex(hashZf);

		return hashZFinal;
	}
	public void onNotify(Object obj, String args) 
	{
		if(obj instanceof Session) {
			if(args=="requestHash") {
				Hash.shouldGeneratePlayerHash=true;
			}
		}
	}

}
