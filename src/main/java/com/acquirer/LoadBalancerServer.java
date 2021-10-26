package com.acquirer;

import com.acquirer.balancer.LoadBalancerImpl;
import com.acquirer.balancer.LoadBalancerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LoadBalancerServer {
    public static void main(String[] args) throws Exception {
        LoadBalancerImpl loadBalancerImpl = new LoadBalancerImpl();
        LoadBalancerInterface balancerStub = (LoadBalancerInterface) UnicastRemoteObject.exportObject(loadBalancerImpl, 0);
        Registry registry = LocateRegistry.createRegistry(2000);
        System.out.println("Registry Created at Port 2000");
        registry.bind("balancer",balancerStub);
        System.out.println("Load balancer started");
    }
}
