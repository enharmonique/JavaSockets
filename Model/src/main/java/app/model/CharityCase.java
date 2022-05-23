package app.model;

import java.io.Serializable;

public class CharityCase implements Comparable<CharityCase>, Serializable, Identifiable<Integer>{
    private int ID;
    private String name;
    private float sum;

    public CharityCase(String name, float sum) {
        this.name = name;
        this.sum = sum;
    }

    public CharityCase() {}

    public CharityCase(String name) {
        this.name = name;
    }

    @Override
    public void setID(Integer integer) {
        this.ID = integer;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "CharityCase{" +
                "name='" + name + '\'' +
                ", sum=" + sum +
                '}';
    }

    @Override
    public int compareTo(CharityCase o) {
        return name.compareTo(o.name);
    }
}
