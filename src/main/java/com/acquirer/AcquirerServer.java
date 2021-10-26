package com.acquirer;

import com.acquirer.balancer.LoadBalancerImpl;
import com.acquirer.rmi.AcquirerProcessImpl;
import com.acquirer.rmi.AcquirerProcessInterface;
import com.acquirer.rmi.AcquirerTimeInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;

public class AcquirerServer {

    public Long getSynchronisedTime(String serverName, String neighbor1,String neighbor2) throws Exception{
        Registry registry = LocateRegistry.getRegistry(2000);
        Long start = Instant.now().toEpochMilli();
        AcquirerTimeInterface neighbour1 = (AcquirerTimeInterface) registry.lookup(neighbor1);
        AcquirerTimeInterface neighbour2 = (AcquirerTimeInterface) registry.lookup(neighbor2);
        Long time1 =  neighbour1.getSystemTime();
        Long time2 = neighbour2.getSystemTime();
        Long end = Instant.now().toEpochMilli();
        Long rtt = end - start;
        Long averageRtt = rtt / 2;
        Long synchronisedServerTime = (Instant.now().toEpochMilli() + averageRtt + time1 + time2) / 3;
        System.out.println("The synchronised "+ serverName +" server time is: " + synchronisedServerTime);
        return synchronisedServerTime;
    }

    // String Args: String stubName,boolean createRegistry, String neighbor1...String neighbor n
    // acquirer1, true
    public static void main(String[] args) throws Exception {
        boolean checkRegistry = Boolean.parseBoolean(args[1]);
        String stubName = args[0];
        AcquirerProcessImpl transactionProcessImpl = new AcquirerProcessImpl();
        AcquirerProcessInterface stub = (AcquirerProcessInterface) UnicastRemoteObject.exportObject(transactionProcessImpl, 0);
        Registry registry = LocateRegistry.getRegistry(2000);
        registry.bind(stubName,stub);
        System.out.println("AcquirerServer Started");
//        Thread.sleep(5000);
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

    }
}
