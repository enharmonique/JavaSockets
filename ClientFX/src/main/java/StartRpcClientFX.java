import gui.AppController;
import gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rpcprotocol.AppServicesRpcProxy;
import app.services.IAppServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/appclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find appclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("app.server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("app.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IAppServices server = new AppServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("LoginView.fxml"));
        Parent root = loader.load();

        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("AppView.fxml"));
        Parent croot = cloader.load();

        AppController appCtrl =
                cloader.<AppController>getController();
        appCtrl.setServer(server);
        //appCtrl.setCharityCases();

        ctrl.setController(appCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP app");
        primaryStage.setScene(new Scene(root, 300, 130));
        primaryStage.show();
    }
}
