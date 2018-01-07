/*
 * Original: Stellar Guides
 * Modified: Pruz (06.01.2018)
 * Reference Libs: stellar-sdk.jar
 * Now this program sends payments to another account by prompting--
 * --USER for "source account seed", "destination account address" and--
 * --"amount of XLM" from "source" account to "destination" account.
 * Handles ioexception
 */
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class buildingTransaction {
   @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

	Network.useTestNetwork();
	//Server (TESTNet)
        Server server = new Server("https://horizon-testnet.stellar.org");
        
    //Asks user for Source account seed    
        Scanner seed1 = new Scanner(System.in);
        System.out.println("\nEnter the source account seed: ");
        String input = seed1.nextLine();
        KeyPair source;

    //Uses user input as source seed
        try {
            source = KeyPair.fromSecretSeed(input);
            TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        } catch (Exception e) {
            throw new RuntimeException("Error!");
        }
        
    //Asks user for destination account address
        Scanner dest1 = new Scanner(System.in);
        System.out.println("\nEnter the destination account add: ");
        String input2 = dest1.nextLine();
        KeyPair destination;

    //Uses user input as destination account address
        try {
            destination = KeyPair.fromAccountId(input2);
            TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        } catch (Exception e) {
            throw new RuntimeException("Error!");
        }
        
    //Asks user for amount of XLM to be sent
        Scanner amt1 = new Scanner(System.in);
        System.out.println("\nEnter the amount of XLM to send: ");
        String amount = amt1.nextLine();

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

	//7. Send to Stellar network to submit transaction
	        try {
	        	SubmitTransactionResponse response = server.submitTransaction(transaction);
	            System.out.println("Success!");
	            System.out.println("\n" + response);
	        }
	        catch (Exception e) {
	        	System.out.println("Something went wrong!");
	        	System.out.println(e.getMessage());
	        }
	    }
	}