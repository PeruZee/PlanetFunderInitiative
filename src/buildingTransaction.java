/*
 * Original: Jed McCaleb
 * Modified: Pruz (05.01.2018)
 * Reference Libs: stellar-sdk.jar
 * This program sends 10 XLM from "source" account to "destination" account
 * Handles ioexception
 */

import java.io.*;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class buildingTransaction {

	public static void main(String[] args) throws IOException {

	Network.useTestNetwork();
        Server server = new Server("https://horizon-testnet.stellar.org");

        KeyPair source = KeyPair.fromSecretSeed("SAWUYVZUFNFRCQNQRI2W7FB26T6LL6RHQE4VQKWHQMRXFQCLHZ4733ZS");
        KeyPair destination = KeyPair.fromAccountId("GCLWP62MDY5HCFE3GCYGDNLHHL5RS57UX6XIS57UIO6K5H6OYGRP54AC");

	//1. Confirm the account ID exists
	        server.accounts().account(destination);

	//2. Load data for the account and get current sequence number
	        AccountResponse sourceAccount = server.accounts().account(source);

	//3. Start building a transaction
	        Transaction transaction = new Transaction.Builder(sourceAccount)

	//4. Add payment operation to account
	                .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), "10"/*XLM*/).build())

	//5. Add memo to transaction
	                .addMemo(Memo.text("TEST_MEMO_HERE"))/*memotext*/
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