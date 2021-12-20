package com.acquirer.balancer;

import com.acquirer.rmi.AcquirerLeaderInterface;
import com.acquirer.rmi.AcquirerLoadInterface;
import com.acquirer.rmi.AcquirerProcessInterface;
import com.model.AcquirerTransaction;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class LoadBalancerImpl implements LoadBalancerInterface {
    int serverCount = 3;
    public static Integer requestCount = 0;
    String serverList[] = {"acquirer1", "acquirer2", "acquirer3"};
    int leader = 1;

    public LoadBalancerImpl() {
    }

    public Map<String, String> processTransaction(AcquirerTransaction acquirerTransaction) throws Exception {
        String freeServer;
        synchronized (requestCount) {
            requestCount++;
            freeServer = serverList[requestCount % serverCount];
        }
        Registry registry = LocateRegistry.getRegistry(2000);

        int minIndex = Integer.MAX_VALUE;
        int minimum = Integer.MAX_VALUE;
        int maxIndex = Integer.MIN_VALUE;
        int maximum = Integer.MIN_VALUE;
        int i = 0;
        List<Integer> failedServersIndex = new ArrayList<>();
        List<Integer> runningServersIndex = new ArrayList<>();
        for (i = 0; i < serverCount; i++) {
            try {
                AcquirerLoadInterface server = (AcquirerLoadInterface) registry.lookup(serverList[i]);
                int reqCount = Math.abs(server.getRequestCount());
                System.out.println( "Server "+ (i+1) +" request count =  " + reqCount);
                if (reqCount < minimum) {
                    minimum = reqCount;
                    minIndex = i;
                }
                if (reqCount > maximum) {
                    maximum = reqCount;
                    maxIndex = i;
                }
                runningServersIndex.add(i+1);
            } catch (Exception e) {
                System.out.println("Server no. "+ (i+1) + " failed to respond.");
                failedServersIndex.add(i+1);
            }
        }

            if(failedServersIndex.size() != 3) {
                if (failedServersIndex.contains(leader)) {
                    AcquirerLeaderInterface server = (AcquirerLeaderInterface) registry.lookup(serverList[maxIndex]);
                    server.setAsLeader();
                    System.out.println("Server "+ (maxIndex+1) + " is the new leader");
                    leader = maxIndex+1;

                }
                else {
//                    System.out.println(leader+" "+runningServersIndex.get(0));
                    if(leader != runningServersIndex.get(0)) {
                        AcquirerLeaderInterface currleader = (AcquirerLeaderInterface) registry.lookup(serverList[leader-1]);
                        currleader.unsetLeader();
                        AcquirerLeaderInterface server = (AcquirerLeaderInterface) registry.lookup(serverList[runningServersIndex.get(0)-1]);
                        server.setAsLeader();
                        System.out.println("Server " + (runningServersIndex.get(0)) + " is the new leader");
                        leader = runningServersIndex.get(0);
                    }
                }
            }



        System.out.println("minIndex = "+ minIndex);
        if( minIndex != Integer.MAX_VALUE) {
            AcquirerProcessInterface acquirertransaction = (AcquirerProcessInterface) registry.lookup(serverList[minIndex]);
            System.out.println("Request no. " + requestCount +
                    " sent to " + serverList[minIndex]);
            return acquirertransaction.processMerchantTransaction(acquirerTransaction);
        } else {
            System.out.println("No servers available");
            return null;
        }
    }
}
