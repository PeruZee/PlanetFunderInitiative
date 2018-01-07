/*
 * Original: Stellar Guides
 * Modified: Pruz (07.01.2018)
 * Reference Libs: stellar-sdk.jar
 * This program shows all payment paging tokens of an account and---
 * --asks USER for the account number // tbd
 *---stream all incoming payments only
 * also lists incoming payments and amount if it's XLM.
 * Handles ioexception.
 */

import java.io.*;
import java.util.concurrent.TimeUnit;
import org.stellar.sdk.*;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public class ReceivePayments {
	
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
//        System.out.println(String.format("\nPaging Token is: %s\n", myToken));
    }

    private static class EventListenerImpl implements EventListener<OperationResponse> {

        private final KeyPair account;

        public EventListenerImpl(KeyPair account) {
            this.account = account;
        }

        @Override
        public void onEvent(OperationResponse payment) {
                // Record the paging token so we can start from here next time.
                savePagingToken(payment.getPagingToken());

                Asset asset = ((PaymentOperationResponse) payment).getAsset();

                boolean xlm = asset.equals(new AssetTypeNative());
                boolean actid = ((PaymentOperationResponse) payment).getFrom().equals(account); 

                String amount = ((PaymentOperationResponse) payment).getAmount();
                String actidTo = ((PaymentOperationResponse) payment).getTo().getAccountId();
                String actidFrom = ((PaymentOperationResponse) payment).getFrom().getAccountId();
                String assetName;

                // The payments stream includes both sent and received payments. We only
                // want to process received payments here.
                
                if (payment instanceof PaymentOperationResponse) {
                    	if (actid == true && xlm == true) {
                    		assetName = "Lumens (XLM)";
                    		StringBuilder output = new StringBuilder();
                    		output.append(amount);
                    		output.append(" ");
                    		output.append(assetName);
                    		output.append(" received to: ");
                    		output.append(actidTo);
                    		System.out.println(output.toString());
/*FOR LATER
*			StringBuilder assetNameBuilder = new StringBuilder();
*			assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getCode());
*			assetNameBuilder.append(":");
*			assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getIssuer().getAccountId());
*			assetName = assetNameBuilder.toString();
*END FOR LATER
*/
                    	}
                    	else if (!(actid == true && xlm == true)) {
                    		assetName = "Lumens (XLM)";
                    		StringBuilder output = new StringBuilder();
                    		output.append(amount);
                    		output.append(" ");
                    		output.append(assetName);
                    		output.append(" sent from: ");
                    		output.append(actidFrom);
                    		System.out.println(output.toString());
                    		}
                    	}
                }
        }

    public static void main(String[] args) throws IOException {

        Network.useTestNetwork();
        Server server = new Server("https://horizon-testnet.stellar.org");
        KeyPair account = KeyPair.fromAccountId("GCLWP62MDY5HCFE3GCYGDNLHHL5RS57UX6XIS57UIO6K5H6OYGRP54AC");
        
        PaymentsRequestBuilder paymentsRequest = server.payments().forAccount(account);
         
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
