import repo.jdbc.CharityCaseDBRepository;
import repo.jdbc.DonationDBRepository;
import repo.jdbc.DonorDBRepository;
import repo.jdbc.UserDBRepository;
import repo.repository.CharityCaseRepository;
import repo.repository.DonationRepository;
import repo.repository.DonorRepository;
import repo.repository.UserRepository;
import server.ServicesImpl;
import app.services.AppException;
import app.services.IAppServices;
import utils.AbstractServer;
import utils.AppRpcConcurrentServer;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) throws AppException {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/appserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find appserver.properties " + e);
            return;
        }

        UserRepository userRepository = new UserDBRepository(serverProps);
        DonorRepository donorRepository = new DonorDBRepository(serverProps);
        CharityCaseRepository charityCaseRepository = new CharityCaseDBRepository(serverProps);
        DonationRepository donationRepository = new DonationDBRepository(serverProps);
        IAppServices appServerImpl = new ServicesImpl(userRepository, donorRepository, charityCaseRepository, donationRepository);

        int appServerPort = defaultPort;
        try {
            appServerPort = Integer.parseInt(serverProps.getProperty("app.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + appServerPort);
        AbstractServer server = new AppRpcConcurrentServer(appServerPort, appServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
