package com.example.behnam.reminder;

public class ReminderModel {
    private int id;
    private int drugID;
    private long startTime;
    private int count;
    private int periodTime;
    private int showCount;

    public ReminderModel(int drugID, long startTime, int count, int periodTime) {
        this.drugID = drugID;
        this.startTime = startTime;
        this.count = count;
        this.periodTime = periodTime;
    }

    public ReminderModel(int drugID, long startTime, int count, int periodTime, int showCount) {
        this.drugID = drugID;
        this.startTime = startTime;
        this.count = count;
        this.periodTime = periodTime;
        this.showCount = showCount;
    }
    public ReminderModel(){

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

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }
}
