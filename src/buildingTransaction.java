/*
 * Original: Stellar Guides
 * Modified: Pruz (06.01.2018)
 * Reference Libs: stellar-sdk.jar
 * Now this program sends payments to another account by prompting--
 * --USER for "source account seed", "destination account address" and--
 * --"amount of XLM" from "source" account to "destination" account.
 * Also displays ledger number, Transaction Hash & Balances at the end.
 * Handles ioexception
 */
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class buildingTransaction {

    private static Scanner scanner = new Scanner( System.in );
    
	public static void main(String[] args) throws IOException {

	Network.useTestNetwork();
    //Server (TESTNet)
        Server server = new Server("https://horizon-testnet.stellar.org");
        
    //Asks user for Source account seed
    	System.out.println("\nEnter the source account seed: ");
    	String input = scanner.nextLine();
        KeyPair source;
        
        try {
            	source = KeyPair.fromSecretSeed(input);
            	TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        }
        catch (Exception e) {
            	throw new RuntimeException("Error! Something went wrong!");
        }
		
    //Asks user for destination account address
		System.out.println("\nEnter the destination account add: ");
    	String input2 = scanner.nextLine();
        KeyPair destination;
    	
        try {
            	destination = KeyPair.fromAccountId(input2);
            	TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        }
        catch (Exception e) {
            	throw new RuntimeException("Error! Something went wrong!");
        }

    //Asks user for amount of Lumens(XLM) to be sent
        try {
        	System.out.println("\nEnter the amount of Lumens(XLM) to send: ");
        	TimeUnit.SECONDS.sleep(2); //wait 2 seconds
    	}
        catch (Exception e) {
        	throw new RuntimeException("Error! Something went wrong!");
    	}
        	String amount = scanner.nextLine();

	//1. Confirm the account ID exists
	        server.accounts().account(destination);

	//2. Load data for the account and get current sequence number
	        AccountResponse sourceAccount = server.accounts().account(source);

	//3. Start building a transaction
	        Transaction transaction = new Transaction.Builder(sourceAccount)

	//4. Add payment operation to account
	        .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), amount).build())

	//5. Add memo to transaction
	        .addMemo(Memo.text("")) //optional memotext
	        .build();

	//6. Sign the transaction using "source" Seed
	        transaction.sign(source);

	//7. Send to Stellar network to submit transaction and get Ledger number & Transaction Hash, extras if errors
	        try {
	        	SubmitTransactionResponse response = server.submitTransaction(transaction);
	            	System.out.println("\nSuccess! " + amount + " Lumens(XLM) were sent!\n");
	            	System.out.println("Ledger Number:\n" + response.getLedger());
	            	System.out.println("Transaction Hash:\n" + response.getHash());
	            	System.out.println("\nExtras: " + response.getExtras());
	        }
	        catch (Exception e) {
	        	System.out.println("\nError! Something went wrong!");
	        	System.out.println(e.getMessage());
	        }
	//8. Get account balances for source account        
	        AccountResponse sourceAccount1 = server.accounts().account(KeyPair.fromAccountId(source.getAccountId()));
	        	System.out.println("\nBalances for account: \n" + source.getAccountId());
	        for (AccountResponse.Balance balance : sourceAccount1.getBalances()) {
	        	System.out.println("\nType: " + balance.getAssetType());
	        	System.out.println("Code: " + balance.getAssetCode());
	        	System.out.println("Balance: " + balance.getBalance());
	      	}
	}
}