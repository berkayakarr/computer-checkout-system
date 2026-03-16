package org.swe212.reservationapp.dto;

public class Computer {
    private int id;
    private String brand;
    private String model;

    public Computer() {}
    public Computer(int id, String brand, String model) {
        this.id = id; this.brand = brand; this.model = model;
    }

    // Getter ve Setterlar
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
}