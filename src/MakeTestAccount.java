/*
 * Initiator: Pruz
 * Reference Libs: stellar-sdk.jar
 * -This program makes an account KeyPair
 * on the Stellar TEST Net using Random Seed
 * -Uses //FreindBot// to fund the account with 10,000 XLM
 * -Displays the transaction details and balances.
 * -Handles IOExceptions
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;

//MakeTestAccount: Class to make the account
public class MakeTestAccount {

	public static void main(String[] args) throws IOException {

		//server: Stellar server (TESTNet)
		Network.useTestNetwork();
		Server server = new Server("https://horizon-testnet.stellar.org");

		//Uses random seed to generate account
		KeyPair pair = KeyPair.random();
		System.out.println("Secret Seed: " + new String(pair.getSecretSeed()));
		System.out.println("Account Add: " + pair.getAccountId());

		//friendbotUrl: Uses TESTNet FriendBot to fund account
		String friendbotUrl = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", pair.getAccountId());
		InputStream response = new URL(friendbotUrl).openStream();

		try {
			@SuppressWarnings("resource")
			String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
			System.out.println("\nSUCCESS! You have a new TESTNet account!\n" + body);
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
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