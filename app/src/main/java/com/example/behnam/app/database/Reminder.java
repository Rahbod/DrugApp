package com.example.behnam.app.database;

public class Reminder {
    private int id;
    private int drugID;
    private long startTime;
    private int count;
    private int periodTime;
    private int ShowCount;

    public Reminder() {
    }

    public int getRowCount() {
        return ShowCount;
    }

    public void setShowCount(int rowCount) {
        this.ShowCount = rowCount;
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

    public void setDrugID(int drug_id) {
        this.drugID = drug_id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
    }
}
