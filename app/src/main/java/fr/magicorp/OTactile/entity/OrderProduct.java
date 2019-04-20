package fr.magicorp.OTactile.entity;

public class OrderProduct {
    private int id;
    private String title;
    private String reference;
    private double priceTTC;
    private int quantity;

    public OrderProduct(int id, String title, String reference, double priceTTC, int quantity) {
        this.id = id;
        this.title = title;
        this.reference = reference;
        this.priceTTC = priceTTC;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getPriceTTC() {
        return priceTTC;
    }

    public void setPriceTTC(double priceTTC) {
        this.priceTTC = priceTTC;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
