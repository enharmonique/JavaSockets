package dto;

import java.io.Serializable;

public class DonationDTO implements Serializable {
    private String donor;
    private String charityCase;
    private String sum;

    public DonationDTO(String donor, String charityCase, String sum) {
        this.donor = donor;
        this.charityCase = charityCase;
        this.sum = sum;
    }

    public String getDonor() {
        return donor;
    }

    public String getCharityCase() {
        return charityCase;
    }

    public String getSum() {
        return sum;
    }

    @Override
    public String toString(){
        return "DonationDTO["+donor+" --> "+charityCase+" : "+sum+"]";
    }
}
