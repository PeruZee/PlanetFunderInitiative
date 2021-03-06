/*
 * Original: Stellar Guides
 * Modified: Pruz (06.01.2018)
 * Reference Libs: stellar-sdk.jar
 * Now this program sends payments to another account by prompting--
 * --USER for "source account seed", "destination account address" and--
 * --"amount of XLM" from "source" account to "destination" account.
 * Also displays Ledger number, Transaction Hash & Source account balances.
 * Handles ioexception.
 */
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class BuildingTransaction {

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
		AccountResponse sender = server.accounts().account(source);

		//3. Start building a transaction
		Transaction transaction = new Transaction.Builder(sender)
				//3.1. Add payment operation to account
				.addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), amount).build())
				//3.2. Add memo to transaction
				.addMemo(Memo.text("")) //optional memotext
				.build();
		//3.3. Sign the transaction using "source" Seed
		transaction.sign(source);

		//4. Send to Stellar network to submit transaction and get Ledger number & Transaction Hash, extras if errors
		try {
			SubmitTransactionResponse response = server.submitTransaction(transaction);
			System.out.println("\nSuccess! You sent " + amount + " Lumens(XLM) to: " + destination.getAccountId()+".");
			System.out.println("\nLedger Number:\n" + response.getLedger());
			System.out.println("Transaction Hash:\n" + response.getHash());
			System.out.println("\nExtras: " + response.getExtras());
			}
		catch (Exception e) {
			System.out.println("\nError! Something went wrong!");
			System.out.println(e.getMessage());
			}

		//5. Asks USER if they want to check account balance and Get account balances for source account
		System.out.println("\nDo you want to check account balance? Type 1 to check");
		String balChoice = scanner.nextLine();
		Integer balCheck = Integer.valueOf(balChoice);

		//5.1. Check if account exists and loads account sequence again
		AccountResponse sourceAccount = server.accounts().account(source);

		//5.2 Checks what USER chose and executes code
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