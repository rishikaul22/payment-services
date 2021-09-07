package com.acquirer.rmi;

import com.model.AcquirerTransaction;
import java.util.HashMap;
import java.util.Map;

public class AcquirerProcessImpl implements AcquirerProcessInterface {
    private int add(int a, int b) {
        return a + b;
    }
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
        }
        return map; //passed by value
    }
}
