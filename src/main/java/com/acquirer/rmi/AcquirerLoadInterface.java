package com.acquirer.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AcquirerLoadInterface extends Remote {
    public int getRequestCount() throws RemoteException;
}
