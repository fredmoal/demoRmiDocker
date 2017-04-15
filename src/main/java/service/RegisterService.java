package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Fred on 14/04/2017.
 */
public interface RegisterService extends Remote {
    public static final String serviceName = "register";

    public void ping() throws RemoteException;
    public void addServerToCluster(String newServerIp) throws RemoteException;
    public void registerServer(String serverName) throws RemoteException;
    public List<String> getServers() throws RemoteException;
    public void updateServers(List<String> s) throws RemoteException;
}
