package service;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fred on 14/04/2017.
 */
public class RegisterServiceImpl implements RegisterService {
    private static List<String> servers = new ArrayList<String>();
    private static String myIp;
    static {
        try {
            myIp = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void ping() throws RemoteException {
    }

    public void addServerToCluster(String newServerIp) throws RemoteException {
        System.out.println("register new server "+newServerIp+" to "+servers);
        for(String server:servers) {
            // prévient tous les autres serveurs sauf moi
            if (! myIp.equals(server)) {
                try {
                    Registry registry = LocateRegistry.getRegistry(server);
                    RegisterService remoteService = (RegisterService) registry.lookup(RegisterService.serviceName);
                    remoteService.registerServer(newServerIp);
                    System.out.println("  register new server to " + server);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }
        registerServer(newServerIp);
    }
    public void registerServer(String serverIp) throws RemoteException {
        servers.add(serverIp);
    }

    public List<String> getServers() throws RemoteException {
        return servers;
    }

    public void updateServers(List<String> servers) throws RemoteException {
        this.servers = servers;
    }
}
