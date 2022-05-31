package dto;

import java.io.Serializable;

public class CharityCaseDTO implements Serializable {
    private String name;
    private String sum;

    public CharityCaseDTO(String name, String sum) {
        this.name = name;
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public String getSum() {
        return sum;
    }

    @Override
    public String toString(){
        return "CharityCaseDTO["+name+" : "+sum+"]";
    }
}
