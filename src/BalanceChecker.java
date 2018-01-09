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

		// Asks USER if they want to check account balance and Get account balances for source account
		System.out.println("\nDo you want to check account balance? Type 1 to check");
		String balChoice = scanner.nextLine();
		Integer balCheck = Integer.valueOf(balChoice);

		// Checks what USER chose and executes code
			if (balCheck == 1) {
				try {
					System.out.println("\n~~~Checking your Account Balance~~~");
					System.out.println("\nBalances for account: " + address.getAccountId());
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