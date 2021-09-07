package com.association.rmi;

import com.model.AssociationTransaction;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class AssociationProcessImpl implements AssociationProcessInterface {
    @Override
    public Map<String, String> processAcquirerTransaction(AssociationTransaction associationTransaction) throws RemoteException {
        System.out.println(associationTransaction.toString());
        Map<String, String> map = new HashMap<>();
        map.put("test","success");
        return map;
    }
}
