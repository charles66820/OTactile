package fr.magicorp.OTactile;

public class Customer {
    private int id;
    private String login;
    private String email;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String avatar;
    private String createdDate;

    public Customer(int id) {
        this.id = id;
    }

    public Customer(int id, String login, String email, String lastName, String firstName, String phoneNumber, String avatar, String createdDate) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
