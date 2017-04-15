package serveur;

import service.RegisterService;
import service.RegisterServiceImpl;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Fred on 14/04/2017.
 */
public class Run {
    public static void main(String... args) {
        // récupère mon adresse IP
        String hostAddress = null;
        try {
            hostAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // deploiement du localservice sur le serveur
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        RegisterService localService = new RegisterServiceImpl();
        RegisterService stub = null;
        try {
            stub = (RegisterService) UnicastRemoteObject.exportObject(localService, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(RegisterService.serviceName, stub);
            System.out.println("service deployed to "+hostAddress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // en argument, l'adresse ip d'un autre serveur déjà up
        if (args.length>0) {
            System.out.println("connecting to cluster by "+args[0]);
            try {
                Registry registry = LocateRegistry.getRegistry(args[0]);
                RegisterService remoteService = (RegisterService) registry.lookup(RegisterService.serviceName);
                remoteService.addServerToCluster(hostAddress);
                // récupère la liste complète des serveurs
                List<String> servers = remoteService.getServers();
                System.out.println("all servers :"+servers);
                localService.updateServers(servers);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            // 1er serveur d'un nouveau cluster : on s'auto-enregistre
            System.out.println("new cluster with "+hostAddress);
            try {
                localService.registerServer(hostAddress);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
