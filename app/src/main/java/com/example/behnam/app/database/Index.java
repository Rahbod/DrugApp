package com.example.behnam.app.database;

public class Index {
    private int id;
    private String name;
    private String fa_name;
    private String brand;

    public Index(int id, String name, String fa_name, String brand) {
        this.id = id;
        this.name = name;
        this.fa_name = fa_name;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFa_name() {
        return fa_name;
    }

    public void setFa_name(String fa_name) {
        this.fa_name = fa_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
