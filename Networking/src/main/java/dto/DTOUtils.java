package dto;

import app.model.CharityCase;
import app.model.Donation;
import app.model.Donor;
import app.model.User;

public class DTOUtils {
    public static User getFromDTO(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        return new User(username, password);
    }

    public static UserDTO getDTO(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return new UserDTO(username, password);
    }

    public static Donor getFromDTO(DonorDTO donorDTO) {
        String name = donorDTO.getName();
        String address = donorDTO.getAddress();
        String phone = donorDTO.getPhone();
        return new Donor(name, address, phone);
    }

    public static DonorDTO getDTO(Donor donor) {
        String name = donor.getName();
        String address = donor.getAddress();
        String phone = donor.getPhone();
        return new DonorDTO(name, address, phone);
    }

    public static DonorDTO[] getDTO(Donor[] donors) {
        DonorDTO[] donorDTO = new DonorDTO[donors.length];
        for (int i = 0; i < donors.length; i++)
            donorDTO[i] = getDTO(donors[i]);
        return donorDTO;
    }

    public static Donor[] getFromDTO(DonorDTO[] donorsDTO) {
        Donor[] allDonors = new Donor[donorsDTO.length];
        for (int i = 0; i < donorsDTO.length; i++) {
            allDonors[i] = getFromDTO(donorsDTO[i]);
        }
        return allDonors;
    }

    public static CharityCase getFromDTO(CharityCaseDTO charityCaseDTO) {
        String name = charityCaseDTO.getName();
        float sum = Float.parseFloat(charityCaseDTO.getSum());
        return new CharityCase(name, sum);
    }

    public static CharityCaseDTO getDTO(CharityCase charityCase) {
        String name = charityCase.getName();
        String sum = Float.toString(charityCase.getSum());
        return new CharityCaseDTO(name, sum);
    }

    public static CharityCaseDTO[] getDTO(CharityCase[] charityCases) {
        CharityCaseDTO[] chDTO = new CharityCaseDTO[charityCases.length];
        for (int i = 0; i < charityCases.length; i++)
            chDTO[i] = getDTO(charityCases[i]);
        return chDTO;
    }

    public static CharityCase[] getFromDTO(CharityCaseDTO[] charityCases) {
        CharityCase[] allCharityCases = new CharityCase[charityCases.length];
        for (int i = 0; i < charityCases.length; i++) {
            allCharityCases[i] = getFromDTO(charityCases[i]);
        }
        return allCharityCases;
    }

    public static Donation getFromDTO(DonationDTO donationDTO) {
        Donor donor = new Donor(donationDTO.getDonor());
        CharityCase charityCase = new CharityCase(donationDTO.getCharityCase());
        float sum = Float.parseFloat(donationDTO.getSum());
        return new Donation(donor, charityCase, sum);
    }

    public static DonationDTO getDTO(Donation donation) {
        String donor = donation.getDonor().getName();
        String charityCase = donation.getCharityCase().getName();
        String sum = Float.toString(donation.getSum());
        return new DonationDTO(donor, charityCase, sum);
    }
}
