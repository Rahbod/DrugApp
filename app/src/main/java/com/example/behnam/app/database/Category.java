package com.example.behnam.app.database;

/**
 * Created by moien on 08/02/2018.
 */

public class Category {
    int id;
    String name;
    int type;

    public Category() {
    }

    public Category(String name, int type) {
        this.name = name;
        this.type = type;
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
}
