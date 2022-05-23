package app.services;

import app.model.Donation;
import app.model.Donor;

public interface IAppObserver {
    void donationAdded(Donation donation) throws AppException;
    void donorAdded(Donor donor) throws AppException;
}
