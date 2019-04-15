package fr.magicorp.OTactile;

public class Order {
    private int id;
    private double total;
    private double shipping;
    private String Address;
    private int Status;

    public Order(int id, double total, double shipping, String address, int status) {
        this.id = id;
        this.total = total;
        this.shipping = shipping;
        Address = address;
        Status = status;
    }

    public int getId() {
        return id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
