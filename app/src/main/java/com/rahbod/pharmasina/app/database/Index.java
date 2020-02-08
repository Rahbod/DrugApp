package com.rahbod.pharmasina.app.database;

public class Index {
    private int id;
    private String name;
    private String fa_name;
    private String brand;
    private String fa_brand;
    private int vegetal;
    private String show_field;
    private String show_field_value;


    public Index(int id, String name, String fa_name, String brand, String fa_brand, int vegetal) {
        this.id = id;
        this.name = name;
        this.fa_name = fa_name;
        this.brand = brand;
        this.fa_brand = fa_brand;
        this.vegetal = vegetal;
    }

    public Index() {
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
        this.show_field = name;
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

    public String getFaBrand() {
        return fa_brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setFaBrand(String fa_brand) {
        this.fa_brand = fa_brand;
    }

    public int getVegetal() {
        return vegetal;
    }

    public void setVegetal(int vegetal) {
        this.vegetal = vegetal;
    }

    public String getShowField() {
        return show_field;
    }

    public void setShowField(String show_field) {
        this.show_field = show_field;
    }

    public String getShowFieldValue() {
        return show_field_value;
    }

    public void setShowFieldValue(String show_field_value) {
        this.show_field_value = show_field_value;
    }
}
