package com.acquirer.rmi;

import com.model.AcquirerTransaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface AcquirerProcessInterface extends Remote {
    public Map<String, String> processMerchantTransaction(AcquirerTransaction acquirerTransaction) throws RemoteException, Exception;

}
