package app.services;

import app.model.CharityCase;
import app.model.Donation;
import app.model.Donor;
import app.model.User;

public interface IAppServices {
    void login(User user, IAppObserver client) throws AppException;
    void logout(User user, IAppObserver client) throws AppException;
    void addDonation(Donation donation) throws AppException;
    void addDonor(Donor donor) throws AppException;
    Donor[] searchDonorByPattern(String pattern) throws AppException;
    CharityCase[] getAllCharityCases(User user) throws AppException;
}
