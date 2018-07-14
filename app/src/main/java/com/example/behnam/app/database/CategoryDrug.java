package com.example.behnam.app.database;

public class CategoryDrug {
    int drugID;
    int categoryID;
    int id;

    public CategoryDrug(int id, int drugID, int categoryID) {
        this.id = id;
        this.drugID = drugID;
        this.categoryID = categoryID;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
