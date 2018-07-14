package com.example.behnam.app.database;

public class Category {
    int id;
    String name;
    int type;
    int parentID;

    public Category() {
    }

    public Category(int id, String name, int type, int parentID) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentID = parentID;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}