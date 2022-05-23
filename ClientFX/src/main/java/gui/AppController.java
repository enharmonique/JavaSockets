package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import app.model.CharityCase;
import app.model.Donation;
import app.model.Donor;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppController implements Initializable, IAppObserver {
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField charityCase;
    @FXML
    private TextField sum;
    @FXML
    private TextField pattern;
    @FXML
    private ListView<Donor> donorListView;
    @FXML
    private ListView<CharityCase> charityCaseListView;

    private IAppServices server;
    private User user;

    public AppController() {
        System.out.println("Constructor AppController");
    }

    public AppController(IAppServices server) {
        this.server = server;
        System.out.println("Constructor AppController with server param");
    }

    public void setServer(IAppServices iAppServices) {
        this.server = iAppServices;
        System.out.println(server);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCharityCases(User user) {
        Platform.runLater(() -> {
            try {
                charityCaseListView.getItems().setAll(server.getAllCharityCases(user));
            } catch (AppException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearTextFields() {
        name.clear();
        address.clear();
        phone.clear();
        charityCase.clear();
        sum.clear();
    }

    void logout() {
        try {
            server.logout(user, this);
        } catch (AppException e) {
            System.out.println("Logout error " + e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT : ");
        System.out.println("END INIT!!!!!!!!!");
    }

    @Override
    public void donationAdded(Donation donation) throws AppException {
        System.out.println("donation addded");
        setCharityCases(user);
        System.out.println("Charity Case Updated " + donation.getCharityCase().getName());
    }

    @Override
    public void donorAdded(Donor donor) throws AppException {
        Platform.runLater(() -> {
            System.out.println("donor addded " + donor.getName());
        });
    }

    public void handleAddDonation(ActionEvent actionEvent) {
        if (Objects.equals(name.getText(), "") ||
                Objects.equals(address.getText(), "") || Objects.equals(phone.getText(), "") ||
                Objects.equals(charityCase.getText(), "") || Objects.equals(sum.getText(), "")) {
            Util.showWarning("Empty fields", "Please fill all fields");
        } else {
            try {
                String nameD = name.getText();
                String addressD = address.getText();
                String phoneD = phone.getText();
                String charityCaseD = charityCase.getText();
                float sumD = Float.parseFloat(sum.getText());
                Donor donor = new Donor(nameD, addressD, phoneD);
                CharityCase charityCase = new CharityCase(charityCaseD, sumD);
                Donation donation = new Donation(donor, charityCase, sumD);
                server.addDonor(donor);
                server.addDonation(donation);
                clearTextFields();
            } catch (AppException e) {
                Util.showWarning("Communication error", "Your server probably closed connection");
            }
        }
    }

    public void handleSearchDonor(ActionEvent actionEvent) throws AppException {
        Platform.runLater(() -> {
            System.out.println("app.persistence.donors found....");
            try {
                donorListView.getItems().setAll(server.searchDonorByPattern(pattern.getText()));
                System.out.println(user);
            } catch (AppException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleCompleteDonorFields(ActionEvent actionEvent) {
        int index = donorListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Util.showWarning("Donor not selected", "Please select a donor from the list");
        } else {
            Donor donor = donorListView.getSelectionModel().getSelectedItem();
            name.setText(donor.getName());
            address.setText(donor.getAddress());
            phone.setText(donor.getPhone());
        }
    }

    public void handleCompleteCharityCaseField(ActionEvent actionEvent) {
        int index = charityCaseListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Util.showWarning("Charity case not selected", "Please select a charity case from the list");
        } else {
            CharityCase charityCaseObj = charityCaseListView.getSelectionModel().getSelectedItem();
            charityCase.setText(charityCaseObj.getName());
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
