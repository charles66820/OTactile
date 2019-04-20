package fr.magicorp.OTactile.entity;

public class Address {
    private int id;
    private String way;
    private String complement;
    private String postalCode;
    private String city;

    public Address(int id, String way, String complement, String postalCode, String city) {
        this.id = id;
        this.way = way;
        this.complement = complement;
        this.postalCode = postalCode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
