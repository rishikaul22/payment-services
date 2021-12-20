package com.client;

import com.Utils;
import com.acquirer.balancer.LoadBalancerImpl;
import com.acquirer.balancer.LoadBalancerInterface;
import com.model.AcquirerTransaction;
import com.acquirer.rmi.AcquirerProcessInterface;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MerchantClient{
    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        Scanner sc = new Scanner(System.in);
        Registry registry = LocateRegistry.getRegistry(2000);

        // add load balancer here
        LoadBalancerInterface loadBalancerInterface = (LoadBalancerInterface) registry.lookup("balancer");
        AcquirerTransaction firstAcquirerTransaction = new AcquirerTransaction();

        int choice = 1;
        while(choice != -1) {
            firstAcquirerTransaction.setId(Utils.generateTransactionID(5));
            System.out.println("Enter\n\t 1 for preset params\n\t 2 for custom params\n\t 3 for custom amount\n\t 4 for 50 transactions\n\t-1 to exit");
            choice = sc.nextInt();
            if (choice == 1) {

                firstAcquirerTransaction.setAmount(1000);
                String [] merchant = Utils.getMerchantDetails();
                firstAcquirerTransaction.setMerchantName(merchant[1]);
                firstAcquirerTransaction.setMerchantAccountId(merchant[0]);
                String [] customer = Utils.generateCustomerDetails();
                firstAcquirerTransaction.setCustomerCardNumber(customer[0]);
                firstAcquirerTransaction.setCustomerName(customer[1]);
            } else if (choice == 2) {
                String temp;
                System.out.println("Enter amount");
                int amt = sc.nextInt();
                firstAcquirerTransaction.setAmount(amt);
                System.out.println("Enter card number");
                temp = sc.next();
                firstAcquirerTransaction.setCustomerCardNumber(temp);
                System.out.println("Enter merchant name");
                temp = sc.next();
                firstAcquirerTransaction.setMerchantName(temp);
                System.out.println("Enter merchant account Id");
                temp = sc.next();
                firstAcquirerTransaction.setMerchantAccountId(temp);
                System.out.println("Enter customer name");

                temp = sc.next();
                firstAcquirerTransaction.setCustomerName(temp);
            }
            if (choice == 3) {
                System.out.println("Enter amount");
                int amt = sc.nextInt();
                firstAcquirerTransaction.setAmount(amt);
                String [] customer = Utils.generateCustomerDetails();
                firstAcquirerTransaction.setCustomerCardNumber(customer[0]);
                firstAcquirerTransaction.setCustomerName(customer[1]);
                String [] merchant = Utils.getMerchantDetails();
                firstAcquirerTransaction.setMerchantName(merchant[1]);
                firstAcquirerTransaction.setMerchantAccountId(merchant[0]);

            }
            if(choice == 4) {
                for(int i = 0; i < 50; i++) {
                    firstAcquirerTransaction.setId(Utils.generateTransactionID(5));
                    firstAcquirerTransaction.setAmount(1000);
                    String [] merchant = Utils.getMerchantDetails();
                    firstAcquirerTransaction.setMerchantName(merchant[1]);
                    firstAcquirerTransaction.setMerchantAccountId(merchant[0]);
                    String [] customer = Utils.generateCustomerDetails();
                    firstAcquirerTransaction.setCustomerCardNumber(customer[0]);
                    firstAcquirerTransaction.setCustomerName(customer[1]);
                    loadBalancerInterface.processTransaction(firstAcquirerTransaction);
                }
            }

            try {
                Map<String, String> returnValue = loadBalancerInterface.processTransaction(firstAcquirerTransaction);
                if(returnValue == null){
                    System.out.println("Sorry, no servers available. :(");
                } else {
                    System.out.println("Response Body: " + returnValue);
                }
            }
            catch (NotBoundException e){
                System.out.println("");
            } catch (Exception e){
                System.out.println(e);
                System.out.println("Sorry, no servers available. :(");
            }
        }
    }
}
