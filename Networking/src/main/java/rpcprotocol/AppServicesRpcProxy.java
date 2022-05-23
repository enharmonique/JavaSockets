package rpcprotocol;

import dto.*;
import app.model.CharityCase;
import app.model.Donation;
import app.model.Donor;
import app.model.User;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppServicesRpcProxy implements IAppServices {
    private String host;
    private int port;

    private IAppObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AppServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws AppException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.NEW_DONATION) {
            Donation donation = DTOUtils.getFromDTO((DonationDTO) response.data());
            try {
                client.donationAdded(donation);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
        if (response.type() == ResponseType.NEW_DONOR) {
            Donor donor = DTOUtils.getFromDTO((DonorDTO) response.data());
            try {
                client.donorAdded(donor);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.NEW_DONATION;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    @Override
    public void login(User user, IAppObserver client) throws AppException {
        initializeConnection();
        UserDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
    }

    @Override
    public void logout(User user, IAppObserver client) throws AppException {
        UserDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public void addDonation(Donation donation) throws AppException {
        DonationDTO donationDTO = DTOUtils.getDTO(donation);
        Request req = new Request.Builder().type(RequestType.ADD_DONATION).data(donationDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public void addDonor(Donor donor) throws AppException {
        DonorDTO donorDTO = DTOUtils.getDTO(donor);
        Request req = new Request.Builder().type(RequestType.ADD_DONOR).data(donorDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public Donor[] searchDonorByPattern(String pattern) throws AppException {
        Request request = new Request.Builder().type(RequestType.SEARCH_DONORS).data(pattern).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        DonorDTO[] donorDTO = (DonorDTO[]) response.data();
        Donor[] donors = DTOUtils.getFromDTO(donorDTO);
        return donors;
    }

    @Override
    public CharityCase[] getAllCharityCases(User user) throws AppException {
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request request = new Request.Builder().type(RequestType.GET_CHARITY_CASES).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        CharityCaseDTO[] charityCasesDTO = (CharityCaseDTO[]) response.data();
        CharityCase[] charityCases = DTOUtils.getFromDTO(charityCasesDTO);
        return charityCases;
    }
}
