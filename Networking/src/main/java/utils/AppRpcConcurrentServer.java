package utils;

import rpcprotocol.AppClientRpcReflectionWorker;
import app.services.IAppServices;

import java.net.Socket;

public class AppRpcConcurrentServer extends AbsConcurrentServer {
    private IAppServices appServices;

    public AppRpcConcurrentServer(int port, IAppServices appServices) {
        super(port);
        this.appServices = appServices;
        System.out.println("AppRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AppClientRpcReflectionWorker worker = new AppClientRpcReflectionWorker(appServices, client);
        //AppClientRpcWorker worker = new AppClientRpcWorker(appServices, rest.client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop() {
        System.out.println("Stopping app.services ...");
    }
}