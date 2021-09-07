package com.acquirer.rmi;

import com.association.rmi.AssociationProcessInterface;
import com.model.AcquirerTransaction;
import com.model.AssociationTransaction;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class AcquirerProcessImpl implements AcquirerProcessInterface {
    @Override
    public Map<String, String> processMerchantTransaction(AcquirerTransaction acquirerTransaction) throws Exception {
        System.out.println("Request Received with Body " + acquirerTransaction);
        Map<String, String> map = new HashMap<>();
        map.put("id", acquirerTransaction.getId());
        map.put("status", "failed");
        map.put("customerName", acquirerTransaction.getCustomerName());
        map.put("merchantName", acquirerTransaction.getMerchantName());
        if(acquirerTransaction.getAmount() <= 1000) {
            map.put("status", "approved");
            Registry registry = LocateRegistry.getRegistry(2001);
            AssociationProcessInterface associationTransaction = (AssociationProcessInterface) registry.lookup("association");
            AssociationTransaction associationTransactionObject = new AssociationTransaction();
            associationTransactionObject.setName("Khushi");
            System.out.println("Response Body: " + associationTransaction.processAcquirerTransaction(associationTransactionObject));
        }
        return map; //passed by value
    }
}
