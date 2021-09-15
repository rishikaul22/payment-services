package com.acquirer.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AcquirerTimeInterface extends Remote {
    public long getSystemTime() throws RemoteException;
}
