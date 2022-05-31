package server;

import app.model.CharityCase;
import app.model.Donation;
import app.model.Donor;
import app.model.User;
import repo.repository.CharityCaseRepository;
import repo.repository.DonationRepository;
import repo.repository.DonorRepository;
import repo.repository.UserRepository;
import app.services.AppException;
import app.services.IAppObserver;
import app.services.IAppServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements IAppServices {
    private UserRepository userRepository;
    private DonorRepository donorRepository;
    private CharityCaseRepository charityCaseRepository;
    private DonationRepository donationRepository;
    private Map<String, IAppObserver> loggedClients;

    public ServicesImpl(UserRepository userRepository, DonorRepository donorRepository, CharityCaseRepository charityCaseRepository, DonationRepository donationRepository) {
        this.userRepository = userRepository;
        this.donorRepository = donorRepository;
        this.charityCaseRepository = charityCaseRepository;
        this.donationRepository = donationRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IAppObserver client) throws AppException {
        User userR = userRepository.findBy(user.getUsername(), user.getPassword());
        if (userR != null) {
            if (loggedClients.get(user.getUsername()) != null)
                throw new AppException("User already logged in.");
            loggedClients.put(user.getUsername(), client);
        } else
            throw new AppException("Authentication failed.");
    }

    @Override
    public synchronized void logout(User user, IAppObserver client) throws AppException {
        IAppObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null)
            throw new AppException("User " + user.getUsername() + " is not logged in.");
    }

    @Override
    public synchronized void addDonation(Donation donation) throws AppException {
        CharityCase oldCharityCase = charityCaseRepository.findByName(donation.getCharityCase().getName());
        if (oldCharityCase != null) {
            donationRepository.save(donation);
            notifyDonationWasAdded(donation);
            CharityCase newCharityCase = new CharityCase(oldCharityCase.getName(), oldCharityCase.getSum() + donation.getSum());
            charityCaseRepository.update(oldCharityCase.getID(), newCharityCase);
        }
        else{
            throw new AppException("Charity Case: " + donation.getCharityCase().getName() + " not found.");
        }
    }

    @Override
    public synchronized void addDonor(Donor donor) throws AppException {
        if (donorRepository.findDonor(donor.getName(), donor.getAddress(), donor.getPhone()) == null) {
            donorRepository.save(donor);
        }
    }

    @Override
    public synchronized Donor[] searchDonorByPattern(String pattern) throws AppException {
        Iterable<Donor> foundDonors = donorRepository.findAllByGivenPattern(pattern);
        Set<Donor> result = new TreeSet<Donor>();
        System.out.println("Donors found for pattern: " + pattern);
        for (Donor donor : foundDonors) {
            result.add(donor);
            System.out.println("+" + donor.getName());
        }
        System.out.println("Size: " + result.size());
        return result.toArray(new Donor[result.size()]);
    }

    @Override
    public synchronized CharityCase[] getAllCharityCases(User user) throws AppException {
        Iterable<CharityCase> allCharityCases = charityCaseRepository.getAll();
        Set<CharityCase> result = new TreeSet<CharityCase>();
        System.out.println("All charity Cases: ");
        for (CharityCase charityCase : allCharityCases) {
            result.add(charityCase);
            System.out.println("+" + charityCase.getName());
        }
        System.out.println("Size: " + result.size());
        return result.toArray(new CharityCase[result.size()]);
    }

    private final int defaultThreadsNo = 2;
    private void notifyDonationWasAdded(Donation donation) throws AppException {
        Iterable<User> users = userRepository.getAll();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (User us : users) {
                IAppObserver client = loggedClients.get(us.getUsername());
                if (client != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying [" + us.getUsername() + "] donation [" + donation + "] was added.");
                            client.donationAdded(donation);
                        } catch (AppException e) {
                            System.out.println("Error notifying users " + e);
                        }
                    });
        }
        executor.shutdown();
    }
}
