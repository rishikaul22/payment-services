package com.association.rmi;
import com.model.AssociationTransaction;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface AssociationProcessInterface extends Remote {
    public Map<String, String> processAcquirerTransaction(AssociationTransaction associationTransaction) throws RemoteException;
}
