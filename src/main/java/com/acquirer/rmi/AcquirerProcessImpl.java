package com.acquirer.rmi;

import com.model.AcquirerTransaction;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AcquirerProcessImpl implements AcquirerProcessInterface, AcquirerTimeInterface {


    private int add(int a, int b) {
        return a + b;
    }

//    public Long getSynchronisedTime(){
//        Long start = Instant.now().toEpochMilli();
//        AcquirerTimeInterface neighbour1 = (AcquirerTimeInterface) registry.lookup(args[2]);
//        AcquirerTimeInterface neighbour2 = (AcquirerTimeInterface) registry.lookup(args[3]);
//        Long time1 =  neighbour1.getSystemTime();
//        Long time2 = neighbour2.getSystemTime();
//        Long end = Instant.now().toEpochMilli();
//        Long rtt = end - start;
//        Long averageRtt = rtt / 2;
//        Long synchronisedServerTime = (Instant.now().toEpochMilli() + averageRtt + time1 + time2) / 3;
//        System.out.println("The synchronised "+ args[0] +" server time is: " + synchronisedServerTime);
//        return synchronisedServerTime;
//    }

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
    @Override
    public long getSystemTime(){
        long time = Instant.now().toEpochMilli();
        System.out.println("This is the current time on the server, "+ time);
        return time;
    }
}
