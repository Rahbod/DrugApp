package com.rahbod.pharmasina.app.database;

public class Reminder {
    private int id;
    private int drugID;
    private long startTime;
    private int count;
    private int periodTime;
    private int showCount;

    public Reminder() {
    }

    public Reminder(int drugID, long startTime, int count, int periodTime, int showCount) {
        this.drugID = drugID;
        this.startTime = startTime;
        this.count = count;
        this.periodTime = periodTime;
        this.showCount = showCount;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int rowCount) {
        this.showCount = rowCount;
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
