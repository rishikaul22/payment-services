package com.acquirer.balancer;

import com.acquirer.rmi.AcquirerLoadInterface;
import com.acquirer.rmi.AcquirerProcessInterface;
import com.model.AcquirerTransaction;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Map;

public class LoadBalancerImpl implements LoadBalancerInterface {
    int serverCount = 3;
    public static Integer requestCount = 0;
    String serverList[] = {"acquirer1", "acquirer2", "acquirer3"};


    public LoadBalancerImpl() {
    }
    public Map<String, String> processTransaction(AcquirerTransaction acquirerTransaction) throws Exception {
        String freeServer;
        synchronized (requestCount) {
            requestCount++;
            freeServer = serverList[requestCount % serverCount];
        }
        Registry registry = LocateRegistry.getRegistry(2000);
        AcquirerLoadInterface server1 = (AcquirerLoadInterface)registry.lookup(serverList[0]);
        AcquirerLoadInterface server2 = (AcquirerLoadInterface)registry.lookup(serverList[1]);
        AcquirerLoadInterface server3 = (AcquirerLoadInterface)registry.lookup(serverList[2]);
        int countServer1 = Math.abs(server1.getRequestCount());
        int countServer2 = Math.abs(server2.getRequestCount());
        int countServer3 = Math.abs(server3.getRequestCount());
        int serverArray[] = new int[3];
        serverArray[0] = countServer1;
        serverArray[1] = countServer2;
        serverArray[2] = countServer3;
        System.out.println(Arrays.toString(serverArray));
        int minIndex = 0;
        int minimum = serverArray[0];
        for(int i = 1; i < 3; i++) {
            if(serverArray[i] < minimum) {
                minimum = serverArray[i];
                minIndex = i;
            }
        }
        AcquirerProcessInterface acquirertransaction = (AcquirerProcessInterface)registry.lookup(serverList[minIndex]);
        System.out.println("Request sent to " + serverList[minIndex]);
        return acquirertransaction.processMerchantTransaction(acquirerTransaction);
    }
}
