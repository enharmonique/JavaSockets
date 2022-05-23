package app.repository;

import app.model.Donor;

import java.util.List;

public interface DonorRepository extends ICrudRepository<Integer, Donor> {
    List<Donor> findAllByGivenPattern(String pattern);
    Donor findDonor(String donorName, String donorAddress, String DonorPhone);
}
