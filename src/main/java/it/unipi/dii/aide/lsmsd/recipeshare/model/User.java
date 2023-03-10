package it.unipi.dii.aide.lsmsd.recipeshare.model;

public abstract class User {
    private String name;
    private String password;

    public User (String name,String password) {
        this.name = name;
        this.password = password;
    }

    public User (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
