package com.client;

import com.model.AcquirerTransaction;
import com.acquirer.rmi.AcquirerProcessInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MerchantClient{
    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        Scanner sc = new Scanner(System.in);
        Registry registry = LocateRegistry.getRegistry(2000);
        AcquirerProcessInterface acquirertransaction = (AcquirerProcessInterface)registry.lookup("acquirer");
        AcquirerTransaction firstAcquirerTransaction = new AcquirerTransaction();
        int choice = 1;
        while(choice != -1) {
            System.out.println("Enter\n\t 1 for preset params\n\t 2 for custom params\n\t 3 for custom amount\n\t-1 to exit");
            choice = sc.nextInt();
            if (choice == 1) {
                firstAcquirerTransaction.setId("1");
                firstAcquirerTransaction.setAmount(1001);
                firstAcquirerTransaction.setCustomerCardNumber("000000000");
                firstAcquirerTransaction.setMerchantName("Amazon");
                firstAcquirerTransaction.setMerchantAccountId("1234");
                firstAcquirerTransaction.setCustomerName("Rishi");
            } else if (choice == 2) {
                System.out.println("Enter Id");
                String temp = sc.next();
                firstAcquirerTransaction.setId(temp);
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
                firstAcquirerTransaction.setId("1");
                System.out.println("Enter amount");
                int amt = sc.nextInt();
                firstAcquirerTransaction.setAmount(amt);
                firstAcquirerTransaction.setCustomerCardNumber("000000000");
                firstAcquirerTransaction.setMerchantName("Amazon");
                firstAcquirerTransaction.setMerchantAccountId("1234");
                firstAcquirerTransaction.setCustomerName("Rishi");
            }

            System.out.println("Response Body: " + acquirertransaction.processMerchantTransaction(firstAcquirerTransaction));
        }
    }
}
