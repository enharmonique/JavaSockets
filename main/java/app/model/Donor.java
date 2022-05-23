package app.model;

import java.io.Serializable;

public class Donor implements Comparable<Donor>, Serializable, Identifiable<Integer> {
    private int ID;
    private String name;
    private String address;
    private String phone;

    public Donor(){}

    public Donor(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Donor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Donor{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public void setID(Integer integer) {
        this.ID = integer;
    }

    @Override
    public Integer getID() {
        return this.ID;
    }

    @Override
    public int compareTo(Donor o) {
        if (name.compareTo(o.name) != 0) {
            return name.compareTo(o.name);
        }
        if (address.compareTo(o.address) != 0) {
            return address.compareTo(o.address);
        }
        return phone.compareTo(o.phone);
    }
}
