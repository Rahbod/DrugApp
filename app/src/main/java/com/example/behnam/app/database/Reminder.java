package com.example.behnam.app.database;

public class Reminder {
    private int id;
    private int drugId;
    private long start_time;
    private int count;
    private int period_time;
    private int row_count;

    public int getRow_count() {
        return row_count;
    }

    public void setRow_count(int row_count) {
        this.row_count = row_count;
    }

    public Reminder() {
    }

    public Reminder(int row_count) {
        this.row_count = row_count;
    }

    public Reminder(int drugId, long start_time, int count, int period_time, int row_count) {
        this.drugId = drugId;
        this.start_time = start_time;
        this.count = count;
        this.period_time = period_time;
        this.row_count = row_count;
    }

    public Reminder(int id, int drugId, long start_time, int count, int period_time, int row_count) {
        this.id = id;
        this.drugId = drugId;
        this.start_time = start_time;
        this.count = count;
        this.period_time = period_time;
        this.row_count = row_count;
    }

    public Reminder(int id, int drug_id, long start_time, int count, int period_time) {
        this.id = id;
        this.drugId = drug_id;
        this.start_time = start_time;
        this.count = count;
        this.period_time = period_time;
    }

    public Reminder(int drug_id, long start_time, int count, int period_time) {
        this.drugId = drug_id;
        this.start_time = start_time;
        this.count = count;
        this.period_time = period_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drug_id) {
        this.drugId = drug_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPeriod_time() {
        return period_time;
    }

    public void setPeriod_time(int period_time) {
        this.period_time = period_time;
    }
}
