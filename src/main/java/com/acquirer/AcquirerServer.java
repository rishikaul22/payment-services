package com.acquirer;

import com.acquirer.rmi.AcquirerProcessImpl;
import com.acquirer.rmi.AcquirerProcessInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AcquirerServer {
    public static void main(String[] args) throws Exception {
        AcquirerProcessImpl transactionProcessImpl = new AcquirerProcessImpl();
        AcquirerProcessInterface stub = (AcquirerProcessInterface) UnicastRemoteObject.exportObject(transactionProcessImpl, 0);
        Registry registry = LocateRegistry.createRegistry(2000);
        registry.bind("acquirer",stub);
//        System.getProperties().put("java.rmi.server.logCalls","true");
        System.out.println("AcquirerServer Started");

    }
}
