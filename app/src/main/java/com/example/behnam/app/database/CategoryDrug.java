package com.example.behnam.app.database;

public class CategoryDrug {
    int drug_id;
    int category_id;
    int type;

    public CategoryDrug(int drug_id, int category_id, int type) {
        this.drug_id = drug_id;
        this.category_id = category_id;
        this.type = type;
    }

    public int getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(int drug_id) {
        this.drug_id = drug_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
