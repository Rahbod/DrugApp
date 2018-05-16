package com.example.behnam.app.database;

public class CategoryDrug {
    int drugID;
    int categoryID;
    int type;

    public CategoryDrug(int drugID, int categoryID, int type) {
        this.drugID = drugID;
        this.categoryID = categoryID;
        this.type = type;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drug_id) {
        this.drugID = drug_id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int category_id) {
        this.categoryID = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
