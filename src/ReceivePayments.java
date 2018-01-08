/*
 * Original: Stellar Guides
 * Modified: Pruz (07.01.2018)
 * Reference Libs: stellar-sdk.jar
 * This program shows all payment paging tokens of an account and---
 * --asks USER for the account number
 *---shows all payment activities on account.
 * Handles ioexception.
 */

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public class ReceivePayments {
	
    private static Scanner scanner = new Scanner( System.in );
	
    public static String myToken = null;
    
    public static String loadLastPagingToken() {
    	try {
    		return myToken;
    		}
       	catch (Exception e) {
        	throw new RuntimeException("Error! Something went wrong!");
       	}
    }    	
    public static void savePagingToken(String pagingToken) {
        myToken = pagingToken;
//        System.out.println(String.format("\nPaging Token is %s", myToken + ":"));
    }

    private static class EventListenerImpl implements EventListener<OperationResponse> {

        private final KeyPair account;

        public EventListenerImpl(KeyPair account) {
            this.account = account;
        }

        @Override
        public void onEvent(OperationResponse payment) {
                // Record the paging token so we can start from here next time (???)
                savePagingToken(payment.getPagingToken());

                Asset asset = ((PaymentOperationResponse) payment).getAsset();

                boolean XLM = asset.equals(new AssetTypeNative());
                boolean actID = ((PaymentOperationResponse) payment).getTo().equals(account); 

                String amount = ((PaymentOperationResponse) payment).getAmount();
                String actIDTo = ((PaymentOperationResponse) payment).getTo().getAccountId();
                String actIDFrom = ((PaymentOperationResponse) payment).getFrom().getAccountId();
                
                String assetName;

                // The payments stream includes both sent and received payments.
                if (payment instanceof PaymentOperationResponse) {
                    	if ((actID == false && XLM == true)) {
                    		System.out.println("[XLM] Payment Activities:");
                    		assetName = "[XLM]";
                    		StringBuilder output = new StringBuilder();
                    		output.append(actIDFrom);
                    		output.append(" SENT: ");
                    		output.append(amount);
                    		output.append(" ");
                    		output.append(assetName);
                    		output.append(" TO: ");
                    		output.append(actIDTo);
                    		System.out.println(output.toString()+"\n");
                    	}
                    	else {
                    		System.out.println("["+((AssetTypeCreditAlphaNum) asset).getCode()+"] Payment Activities:");
                			StringBuilder assetNameBuilder = new StringBuilder();
                			assetNameBuilder.append("["+((AssetTypeCreditAlphaNum) asset).getCode()+"]");
//                			assetNameBuilder.append(" : ");
//               			assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getIssuer().getAccountId()+")");
                			assetName = assetNameBuilder.toString();
                    		StringBuilder output = new StringBuilder();
                    		output.append(actIDFrom);
                    		output.append(" SENT: ");
                    		output.append(amount);
                    		output.append(" ");
                    		output.append(assetName);
                    		output.append(" TO: ");
                    		output.append(actIDTo);
                    		System.out.println(output.toString()+"\n");
                    		}
                    	}
                }
        }
    public static void main(String[] args) throws IOException {

        Network.useTestNetwork();
        Server server = new Server("https://horizon-testnet.stellar.org");
        
        // Asks user for Account Address
        System.out.println("\n~~~ This program shows Balances and Payments in one account ~~~");
        System.out.println("\nENTER THE ACCOUNT ADDRESS TO CHECK: ");
        String input = scanner.nextLine();

        KeyPair account;

        //Uses user input
        try {
        	account = KeyPair.fromAccountId(input);
            TimeUnit.SECONDS.sleep(2); //wait 2 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException("Error! Something went wrong!");
        }
        
    	//1. Confirm the account ID exists
        server.accounts().account(account);
        
        PaymentsRequestBuilder paymentsRequest = server.payments().forAccount(account);

        //2. Load data for the account and get current sequence number
        AccountResponse sourceAccount = server.accounts().account(account);
  
    	//3. Get account balances for source account
        System.out.println("\n~~~ Displaying Balances for account: " + account.getAccountId()+" ~~~\n");
	        for (AccountResponse.Balance balance : sourceAccount.getBalances()) {
	        		System.out.println("Type: " + balance.getAssetType());
	        		System.out.println("Code: " + balance.getAssetCode());
	        		System.out.println("Limit: " + balance.getLimit());
	        		System.out.println("Balance: " + balance.getBalance());
      	}
/*	later        
	    // write to a file the data
	        String content = (String.format("\nPaging Token is: %s", myToken));
	        File file = new File("./src/Payments.txt");
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
        }
*/	System.out.println("\n~~~ Displaying Payment Activities for account: "+ account.getAccountId()+" ~~~\n");        
        // Token work(???)
        String lastToken = loadLastPagingToken();
        
       	try {
       		if (lastToken != null) {
                paymentsRequest.cursor(lastToken);
              } 
              paymentsRequest.stream(new EventListenerImpl(account));
			TimeUnit.SECONDS.sleep(2); //wait 2 seconds
		}
       	catch (InterruptedException e) {
        	throw new RuntimeException("Error! Something went wrong!");
       	}
 
    }
}