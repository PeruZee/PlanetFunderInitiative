/*
 * Original: Stellar
 * Modified: Pruz (07.01.2018)
 * Reference Libs: stellar-sdk.jar
 * This program Allows Asset to be Trusted
 * Prompts user for their account seed and
 * also prompts user for amount of asset (TBC) to trust and
 * also shows them their previous balances.
 * Handles IOException.
 */

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class TrustChange {

    private static Scanner scanner = new Scanner( System.in );

	public static void main(String[] args) throws IOException {

		Network.useTestNetwork();
		Server server = new Server("https://horizon-testnet.stellar.org");

		// Issuing Account Address for Asset: TBC
		KeyPair  issuingKeys = KeyPair.fromAccountId("GDZHWATCLKQTIIHEKFJNTJCR234NE6UIZSJKD2VDEPVTJ3ZYZF7CQMZY");

	    // Asks user for Source account seed
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

        // Asks user for amount of Asset (TBC) to Trust
        try {
        	System.out.println("\nEnter the amount of Asset(TBC) to Trust: ");
        	TimeUnit.SECONDS.sleep(2); //wait 2 seconds
    	}
        catch (Exception e) {
        	throw new RuntimeException("Error! Something went wrong!");
    	}
        	String amount = scanner.nextLine();

		// Represent the Asset
		Asset TBC = Asset.createNonNativeAsset("TBC", issuingKeys);

		// Make the receiving account trust the asset
		AccountResponse receiving = null;
		try {
			receiving = server.accounts().account(source);
		}
	    catch (Exception e) {
	        	throw new RuntimeException("Error! Something went wrong!");
	       	}

		Transaction allowTBC = new Transaction.Builder(receiving)
		  .addOperation(
		// ChangeTrust operation creates (or alters) a TrustLine
		// Second parameter limits the amount
		    new ChangeTrustOperation.Builder(TBC, amount).build())
		  .build();
		allowTBC.sign(source);

		// Display Ledger number and Transaction Hash if it was a success
		try {
			SubmitTransactionResponse res = server.submitTransaction(allowTBC);
			if (!res.isSuccess() == false) {
			System.out.println("\nCongrats! It was a success!");
        	System.out.println("\nLedger Number:\n" + res.getLedger());
        	System.out.println("Transaction Hash:\n" + res.getHash());
        	TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		} 
	    catch (Exception e) {
        	throw new RuntimeException("\nError! Something went wrong!");
		}

		// Asks USER if they want to check account balance and Get account balances for source account
		System.out.println("\nDo you want to check account balance? Type 1 to check");
		String balChoice = scanner.nextLine();
		Integer balCheck = Integer.valueOf(balChoice);

		// Check if account exists and loads account sequence again
		AccountResponse sourceAccount = server.accounts().account(source);

		// Checks what USER chose and executes code
			if (balCheck == 1) {
				try {
					System.out.println("\n~~~Checking your Account Balance~~~");
					System.out.println("\nBalances for account: " + source.getAccountId());
					for (AccountResponse.Balance balance : sourceAccount.getBalances()) {
						System.out.println("\nType: " + balance.getAssetType());
						System.out.println("Code: " + balance.getAssetCode());
						System.out.println("Limit: " + balance.getLimit());
						System.out.println("Balance: " + balance.getBalance());
						}
					}
				catch (Exception e) {
					throw new RuntimeException("\nError! Something went wrong!");
					}
			}
			else {
				System.out.println("\n~~~You chose to not check account balances! Goodbye!~~~");
				}
	        }
	}