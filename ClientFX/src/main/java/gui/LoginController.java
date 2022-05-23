package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import app.model.User;
import app.services.AppException;
import app.services.IAppServices;

public class LoginController {
    private IAppServices server;
    private AppController appController;
    private User currentUser;

    @FXML
    TextField username;
    @FXML
    TextField password;

    Parent mainParent;

    public void setServer(IAppServices iAppServices) {
        server = iAppServices;
    }

    public void setParent(Parent parent) {
        mainParent = parent;
    }


    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setController(AppController appController) {
        this.appController = appController;
    }

    public void pressLogin(ActionEvent actionEvent) {
        String inputUsername = username.getText();
        String inputPassword = password.getText();
        currentUser = new User(inputUsername, inputPassword);
        try {
            server.login(currentUser, appController);
            Stage stage = new Stage();
            stage.setTitle("Window for " + currentUser.getUsername());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    appController.logout();
                    System.exit(0);
                }
            });
            stage.show();
            appController.setUser(currentUser);
            appController.setCharityCases(currentUser);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (AppException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }

    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }
}
