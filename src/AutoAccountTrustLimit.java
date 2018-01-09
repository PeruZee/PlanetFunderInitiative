/*
 * Initiator: Pruz
 * Reference Libs: stellar-sdk.jar
 * -This program makes an account KeyPair
 * on the Stellar TEST Net using Random Seed
 * -Uses //FreindBot// to fund the account with 10,000 XLM
 * -Allows Asset(TBC) to be Trusted by adding a TrustLine
 * -Prompts user for amount of asset (TBC) to trust and
 * -Displays the transaction details and balances at the end.
 * -Handles IOExceptions
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

//AutoAccountTrustLimit: Class to make the account
public class AutoAccountTrustLimit {

	private static Scanner scanner = new Scanner( System.in );

	public static void main(String[] args) throws IOException {

		//server: Stellar server (TESTNet)
		Network.useTestNetwork();
		Server server = new Server("https://horizon-testnet.stellar.org");

		//Uses random seed to generate account
		KeyPair pair = KeyPair.random();
		System.out.println("Secret Seed: " + new String(pair.getSecretSeed()));
		System.out.println("Account Add: " + pair.getAccountId());

		// Issuing Account Address for Asset: TBC
		KeyPair  issuingKeys = KeyPair.fromAccountId("GDZHWATCLKQTIIHEKFJNTJCR234NE6UIZSJKD2VDEPVTJ3ZYZF7CQMZY");

		//friendbotUrl: Uses TESTNet FriendBot to fund account and display        
		try {
			String friendbotUrl = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", pair.getAccountId());
			InputStream response = new URL(friendbotUrl).openStream();
			@SuppressWarnings("resource")
			String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
			System.out.println("\nSUCCESS! You have a new TESTNet account!\n" + body);
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Uses generated seed to commence Trust operation
		System.out.println("\n~~~Using Secret Seed to Trust Asset~~~");
		KeyPair source;

		try {
			source = KeyPair.fromSecretSeed(pair.getSecretSeed());
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}

		// Asks user for amount of Asset (TBC) to Trust
		try {
			System.out.println("\nEnter the amount of TBC(Currency) to Trust: ");
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}
		String amount = scanner.nextLine();

		//1. Represent the Asset
		Asset TBC = Asset.createNonNativeAsset("TBC", issuingKeys);

		//2. Make the receiving account trust the asset
		AccountResponse receiving = null;
		try {
			receiving = server.accounts().account(source);
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
			}
		Transaction allowTBC = new Transaction.Builder(receiving)
				.addOperation(
		//2.1. ChangeTrust operation creates (or alters) a TrustLine
		// Second parameter limits the amount
						new ChangeTrustOperation.Builder(TBC, amount).build())
				.build();
		//2.2 sign using source Secret Seed
		allowTBC.sign(source);

		//3. Display Ledger number and Transaction Hash if it was a success
		try {
			SubmitTransactionResponse res = server.submitTransaction(allowTBC);
			if (!res.isSuccess() == false) {
				System.out.println("\nCONGRATULATIONS! You can now use [TBC: "+TBC+"] Cryptocurrency!");
				System.out.println("\nLedger Number:\n" + res.getLedger());
				System.out.println("Transaction Hash:\n" + res.getHash());
				TimeUnit.SECONDS.sleep(1); //wait 1 second
				}
			} 
		catch (Exception e) {
			throw new RuntimeException("\nError! Something went wrong! Please try again.");
			}

		// balance, AccountResponse: fetches account balances for generated account by getting current sequence
		AccountResponse account = server.accounts().account(KeyPair.fromAccountId(pair.getAccountId()));
		System.out.println("\nBalances for account: \n" + pair.getAccountId());
		for (AccountResponse.Balance balance : account.getBalances()) {
			System.out.println("\nType: " + balance.getAssetType());
			System.out.println("Code: " + balance.getAssetCode());
			System.out.println("Limit: " + balance.getLimit());
			System.out.println("Balance: " + balance.getBalance());
			}
		}
	}