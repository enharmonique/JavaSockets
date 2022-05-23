package app.model;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable, Identifiable<Integer>{
    private int ID;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
        this.password = "";
    }

    public User(){
        this.username = "";
        this.password = "";
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public boolean equals(Object obj) {
        if (obj instanceof User){
            User u = (User)obj;
            return username.equals(u.username);
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
