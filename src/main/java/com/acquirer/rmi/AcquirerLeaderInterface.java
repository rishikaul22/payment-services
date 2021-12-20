package com.acquirer.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AcquirerLeaderInterface  extends Remote {

    public void setAsLeader() throws RemoteException;

    public void unsetLeader() throws RemoteException;

    public void updateServersAboutNewLeader(String server) throws Exception;
}
