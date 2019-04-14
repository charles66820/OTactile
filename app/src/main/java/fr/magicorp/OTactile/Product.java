package fr.magicorp.OTactile;

public class Product {
    private int id;
    private String title;
    private double priceTTC;
    private String reference;
    private int quantity;
    private String description;
    private String mainPicture;
    private float opinionsAvg;

    public Product() { }

    public Product(String title, double priceTTC, String reference, int quantity, String description, String mainPicture, float opinionsAvg) {
        this.title = title;
        this.priceTTC = priceTTC;
        this.reference = reference;
        this.quantity = quantity;
        this.description = description;
        this.mainPicture = mainPicture;
        this.opinionsAvg = opinionsAvg;
    }

    public Product(int id, String title, Float priceTTC, String reference, int quantity, String description, String mainPicture, float opinionsAvg) {
        this.id = id;
        this.title = title;
        this.priceTTC = priceTTC;
        this.reference = reference;
        this.quantity = quantity;
        this.description = description;
        this.mainPicture = mainPicture;
        this.opinionsAvg = opinionsAvg;
    }

    public Product(int id, String title, double priceTTC, int quantity, String mainPicture, float opinionAVG) {
        this.id = id;
        this.title = title;
        this.priceTTC = priceTTC;
        this.reference = null;
        this.quantity = quantity;
        this.description = null;
        this.mainPicture = mainPicture;
        this.opinionsAvg = opinionAVG;
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

    public double getPriceTTC() {
        return priceTTC;
    }

    public void setPriceTTC(double priceTTC) {
        this.priceTTC = priceTTC;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public float getOpinionsAvg() {
        return opinionsAvg;
    }

    public void setOpinionsAvg(float opinionsAvg) {
        this.opinionsAvg = opinionsAvg;
    }
}
