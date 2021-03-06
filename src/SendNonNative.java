/*
 * Original: Stellar
 * Code: Pruz
 * Reference Libs: stellar-sdk.jar
 * This program Allows NonNativeAsset to be sent
 * Prompts user for their account seed and destination account
 * also prompts user for amount of asset (TBC) to send and
 * shows them their previous balances.
 * Handles IOException.
 */

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class SendNonNative {

	private static Scanner scanner = new Scanner( System.in );

	public static void main(String[] args) throws IOException {

		Network.useTestNetwork();
		Server server = new Server("https://horizon-testnet.stellar.org");

		// Issuing Account Address for Asset: TBC
		KeyPair issuingKeys = KeyPair.fromAccountId("GDZHWATCLKQTIIHEKFJNTJCR234NE6UIZSJKD2VDEPVTJ3ZYZF7CQMZY");

		// Asks user for Sending account seed
		System.out.println("\nEnter the Source account Seed: ");
		String input = scanner.nextLine();
		KeyPair source;

		try {
			source = KeyPair.fromSecretSeed(input);
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks user for destination account address
		System.out.println("\nEnter the destination account: ");
		String input2 = scanner.nextLine();
		KeyPair destination;

		try {
			destination = KeyPair.fromAccountId(input2);
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks user for amount of Asset (TBC) to Send
		try {
			System.out.println("\nEnter the amount of Asset(TBC) to Send: ");
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}
		String amount = scanner.nextLine();

		// Represent the Asset
		Asset TBC = Asset.createNonNativeAsset("TBC", issuingKeys);

		// Load the source account
		AccountResponse receiving = null;
		try {
			receiving = server.accounts().account(source);
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Send Transaction to Stellar Network
		Transaction sendTBC = new Transaction.Builder(receiving)
				.addOperation(new PaymentOperation.Builder(destination, TBC, amount).build())
				.build();
		sendTBC.sign(source);

		// Display Ledger number and Transaction Hash if it was a success
		try {
			SubmitTransactionResponse res = server.submitTransaction(sendTBC);
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

		//Check if account exists and loads account sequence again
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