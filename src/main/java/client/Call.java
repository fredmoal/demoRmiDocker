package client;

import service.RegisterService;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Fred on 14/04/2017.
 */
public class Call {
    public static void main(String... args) {
        if (args.length<1) {
            System.out.println("usage : java client.Call ipServeur");
            System.exit(-1);
        }
        try {
            System.out.println("client connecting to "+args[0]);
            Registry registry = LocateRegistry.getRegistry(args[0]);
            RegisterService h = (RegisterService) registry.lookup(RegisterService.serviceName);
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            System.out.println("registered servers on "+args[0]+" :");
            for(String server : h.getServers()) {
                System.out.println("  "+server);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}
