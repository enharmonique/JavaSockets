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

public class AppClientRpcWorker implements Runnable, IAppObserver {
    private IAppServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AppClientRpcWorker(IAppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void donationAdded(Donation donation) throws AppException {
        DonationDTO donationDTO = DTOUtils.getDTO(donation);
        Response resp = new Response.Builder().type(ResponseType.NEW_DONATION).data(donationDTO).build();
        System.out.println("Donation added " + donation);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new AppException("Sending error: "+e);
        }
    }

    @Override
    public void donorAdded(Donor donor) throws AppException {
        DonorDTO donorDTO = DTOUtils.getDTO(donor);
        Response resp = new Response.Builder().type(ResponseType.NEW_DONOR).data(donorDTO).build();
        System.out.println("Donor added " + donor);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new AppException("Sending error: "+e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request... " + request.type());
            UserDTO udto = (UserDTO) request.data();
            User user = DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request");
            UserDTO udto = (UserDTO) request.data();
            User user = DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ADD_DONATION) {
            System.out.println("AddDonationRequest ...");
            DonationDTO donationDTO = (DonationDTO)request.data();
            Donation donation = DTOUtils.getFromDTO(donationDTO);
            try {
                server.addDonation(donation);
                return okResponse;
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ADD_DONOR) {
            System.out.println("AddDonorRequest...");
            DonorDTO donorDTO = (DonorDTO) request.data();
            Donor donor = DTOUtils.getFromDTO(donorDTO);
            try {
                server.addDonor(donor);
                return okResponse;
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.GET_CHARITY_CASES) {
            System.out.println("GetCharityCasesRequest...");
            UserDTO udto = (UserDTO)request.data();
            User user = DTOUtils.getFromDTO(udto);
            try {
                CharityCase[] charityCases = server.getAllCharityCases(user);
                CharityCaseDTO[] charityCaseDTO = DTOUtils.getDTO(charityCases);
                return new Response.Builder().type(ResponseType.GET_CHARITY_CASES).data(charityCaseDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.SEARCH_DONORS) {
            System.out.println("SearchDonorsRequest...");
            String pattern = (String) request.data();
            try {
                Donor[] donors = server.searchDonorByPattern(pattern);
                DonorDTO[] donorDTO = DTOUtils.getDTO(donors);
                return new Response.Builder().type(ResponseType.FOUND_DONORS).data(donorDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }
}
