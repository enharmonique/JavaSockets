package app.model;

import java.io.Serializable;

public class Donation implements Comparable<Donation>, Serializable, Identifiable<Integer>{
    private int ID;
    private Donor donor;
    private CharityCase charityCase;
    private float sum;

    public Donation(Donor donor, CharityCase charityCase, float sum) {
        this.donor = donor;
        this.charityCase = charityCase;
        this.sum = sum;
    }

    @Override
    public void setID(Integer integer) {
        this.ID = integer;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public CharityCase getCharityCase() {
        return charityCase;
    }

    public void setCharityCase(CharityCase charityCase) {
        this.charityCase = charityCase;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donor=" + donor.getName() +
                ", charityCase=" + charityCase.getName() +
                ", sum=" + sum +
                '}';
    }

    @Override
    public int compareTo(Donation o) {
        int donorNameDif = donor.getName().compareTo(o.donor.getName());
        if (donorNameDif != 0){
            return donorNameDif;
        }
        return charityCase.getName().compareTo(o.charityCase.getName());
    }
}
