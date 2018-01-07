/*
 * Original: Stellar Guides
 * Modified: Pruz (07.01.2018)
 * Reference Libs: stellar-sdk.jar
 * This programs prompts USER for account address and checks account balance.
 * Handles ioexception
 */
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;

public class BalanceChecker {

    private static Scanner scanner = new Scanner( System.in );

	public static void main(String[] args) throws IOException {

	Network.useTestNetwork();
    //Server (TESTNet)
        Server server = new Server("https://horizon-testnet.stellar.org");
        
    //1. Asks user for Account Address
        System.out.println("\nThis program checks account Balance.");
        System.out.println("Enter the account address: ");
        String input = scanner.nextLine();

        KeyPair address;

    //Uses user input
        try {
        	address = KeyPair.fromAccountId(input);
            TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        } catch (Exception e) {
            throw new RuntimeException("Error!");
        }
    //2. Load data for the account and get current sequence number
        AccountResponse sourceAccount = server.accounts().account(address);
	//3. Get account balances for source account
	        System.out.println("\nBalances for account: \n" + address.getAccountId());
	        for (AccountResponse.Balance balance : sourceAccount.getBalances()) {
	        		System.out.println("\nType: " + balance.getAssetType());
	        		System.out.println("Code: " + balance.getAssetCode());
	        		System.out.println("Balance: " + balance.getBalance());
	      	}
	}
}