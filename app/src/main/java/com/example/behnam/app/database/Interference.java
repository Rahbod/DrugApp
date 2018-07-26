package com.example.behnam.app.database;

public class Interference {

    private int id;
    private int drugID;
    private String model;
    private int modelID;
    private String text;

    public Interference(int id, int drugID, String model, int modelID, String text) {
        this.id = id;
        this.drugID = drugID;
        this.model = model;
        this.modelID = modelID;
        this.text = text;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
