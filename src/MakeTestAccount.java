/*
 * Initiator: Pruz
 * Reference Libs: stellar-sdk.jar
 * This program makes an account on the Stellar TEST Net
 * Also uses //freindbot// to fund the account with 10,000 XLM
 * Displays the transaction details and balances at the end
 * Handles ioexceptions
 */
import java.net.*;
import java.io.*;
import java.util.*;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;

//MakeTestAccount: Class to make the account
public class MakeTestAccount {
	
	public static void main(String[] args) throws IOException {
    	
    	//Uses random seed to generate account
        KeyPair pair = KeyPair.random();
        System.out.println("Secret Seed: " + new String(pair.getSecretSeed()));
        System.out.println("Account Add: " + pair.getAccountId());
        
        
        //friendbotUrl: Uses testnet friendbot to fund account
        String friendbotUrl = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", pair.getAccountId());
        InputStream response = new URL(friendbotUrl).openStream();
        
        @SuppressWarnings("resource") //suppressing resource warning
	String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
        System.out.println("\nSUCCESS! You have a new TESTNet account!\n" + body);
        
        
        //server: Stellar server (testnet)
        Server server = new Server("https://horizon-testnet.stellar.org");
        
        //balance: fetches account balances for generated account
        AccountResponse account = server.accounts().account(KeyPair.fromAccountId(pair.getAccountId()));
        System.out.println("\nBalances for account: \n" + pair.getAccountId());
        for (AccountResponse.Balance balance : account.getBalances()) {
        	System.out.println("\nType: " + balance.getAssetType());
        	System.out.println("Code: " + balance.getAssetCode());
        	System.out.println("Balance: " + balance.getBalance());
        }
    }
}
