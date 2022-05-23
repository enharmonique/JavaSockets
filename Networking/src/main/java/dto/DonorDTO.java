package dto;

import java.io.Serializable;

public class DonorDTO implements Serializable {
    private String name;
    private String address;
    private String phone;

    public DonorDTO(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString(){
        return "DonorDTO["+name+" ; "+address+" ; "+phone+"]";
    }
}
