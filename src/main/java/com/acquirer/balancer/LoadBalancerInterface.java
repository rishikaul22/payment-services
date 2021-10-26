package com.acquirer.balancer;

import com.model.AcquirerTransaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface LoadBalancerInterface extends Remote {
    public Map<String, String> processTransaction(AcquirerTransaction firstAcquirerTransaction) throws Exception;
}
