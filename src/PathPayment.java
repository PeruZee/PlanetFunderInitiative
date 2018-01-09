/*
 * Original Code: Pruz
 * Reference Libs: stellar-sdk.jar
 * This program Allows SENDER to:
 * send one AssetType to DESTINATION account as another AssetType using PathPayments
 * Also prompts for account balance check
 * Handles IOException.
 */

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class PathPayment {

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
			throw new RuntimeException("Error! Something went wrong! Please try again!");
			}

		// Load the source account
		AccountResponse receiving = null;
		try {
			receiving = server.accounts().account(source);
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong! Please try again!");
			}

		// Represent the Assets
		Asset TBC = Asset.createNonNativeAsset("TBC", issuingKeys);
		Asset XLM = new AssetTypeNative();

		// Asks USER to choose between option [1: TBC -> XLM] || [option 2: XLM ->TBC]
		System.out.println("\n~~~Please choose an option([1: TBC -> XLM] OR [2: XLM -> TBC]):");
		String opt = scanner.nextLine();
		Integer option = Integer.valueOf(opt);

		//OPTION 1: TBC -> XLM
		if (option == 1) {
		// Asks user for amount of Asset (TBC) to Send
			try {
				System.out.println("\nEnter the amount of Asset (TBC) to Send: ");
				TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
			catch (Exception e) {
				throw new RuntimeException("\nError! Something went wrong!");
				}
				String sendMax = scanner.nextLine();
		// Asks user for amount of Destination Asset (XLM) to receive
			try {
				System.out.println("\nEnter the amount of Asset (XLM) to receive: ");
				TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
			catch (Exception e) {
				throw new RuntimeException("\nError! Something went wrong!");
			}
		String destAmount = scanner.nextLine();
		// Send PathPayment Transaction to Stellar Network (sendAsset, sendMax, destination, destAsset, destAmount)
		Transaction sendTBC = new Transaction.Builder(receiving)
				.addOperation(new PathPaymentOperation.Builder(TBC, sendMax, destination, XLM, destAmount).build())
				.build();
		sendTBC.sign(source);
		// Display Ledger number and Transaction Hash if it was a success
		try {
			SubmitTransactionResponse res = server.submitTransaction(sendTBC);
			if (!res.isSuccess() == false) {
				System.out.println
				("\nCongratulations! Path Payment was a success! You sent "+sendMax+" TBC as "+destAmount+" XLM.");
				System.out.println("\nLedger Number:\n" + res.getLedger());
				System.out.println("Transaction Hash:\n" + res.getHash());
				TimeUnit.SECONDS.sleep(1); //wait 1 second
				}
			else {
				System.out.println("\nOops! Path Payment did not work! Please try again!");
				TimeUnit.SECONDS.sleep(1); //wait 1 second
				}
			}
		catch (Exception e) {
			throw new RuntimeException("\nError! Something went wrong!");
			}
		}
		//END OPTION 1 && BEGIN OPTION 2: XLM -> TBC
		// Asks user for amount of Asset (XLM) to Send
		else if (option == 2) {
			try {
				System.out.println("\nEnter the amount of Asset (XLM) to Send: ");
				TimeUnit.SECONDS.sleep(1); //wait 1 second
				}
			catch (Exception e) {
				throw new RuntimeException("\nError! Something went wrong!");
				}
			String sendMax = scanner.nextLine();
		// Asks user for amount of Destination Asset (TBC) to receive
			try {
				System.out.println("\nEnter the amount of Asset (TBC) to receive: ");
				TimeUnit.SECONDS.sleep(1); //wait 1 second
				}
			catch (Exception e) {
				throw new RuntimeException("\nError! Something went wrong!");
				}
			String destAmount = scanner.nextLine();
		// Send PathPayment Transaction to Stellar Network (sendAsset, sendMax, destination, destAsset, destAmount)
			Transaction sendTBC = new Transaction.Builder(receiving)
					.addOperation(new PathPaymentOperation.Builder(XLM, sendMax, destination, TBC, destAmount).build())
					.build();
			sendTBC.sign(source);
		// Display Ledger number and Transaction Hash if it was a success
			try {
				SubmitTransactionResponse res = server.submitTransaction(sendTBC);
				if (!res.isSuccess() == false) {
					System.out.println
					("\nCongratulations! Path Payment was a success! You sent "+sendMax+" XLM as "+destAmount+" TBC.");
					System.out.println("\nLedger Number:\n" + res.getLedger());
					System.out.println("Transaction Hash:\n" + res.getHash());
					TimeUnit.SECONDS.sleep(1); //wait 1 second
					}
				else {
					System.out.println("\nOops! Path Payment did not work! Please try again!");
					TimeUnit.SECONDS.sleep(1); //wait 1 second
					}
				}
			catch (Exception e) {
				throw new RuntimeException("\nError! Something went wrong!");
				}
			}
		//END OPTION 2
		else {
			System.out.println("\nOops! Path Payment did not work! Please try again!");
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