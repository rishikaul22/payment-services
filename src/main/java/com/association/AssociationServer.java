package com.association;

import com.acquirer.rmi.AcquirerProcessImpl;
import com.acquirer.rmi.AcquirerProcessInterface;
import com.association.rmi.AssociationProcessImpl;
import com.association.rmi.AssociationProcessInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AssociationServer {

    public static void main(String[] args) throws Exception{
        AssociationProcessImpl associationProcessImpl = new AssociationProcessImpl();
        AssociationProcessInterface stub = (AssociationProcessInterface) UnicastRemoteObject.exportObject(associationProcessImpl, 0);
        Registry registry = LocateRegistry.createRegistry(2001);
        registry.bind("association", stub);
        System.out.println("AssociationServer started");
    }
}
