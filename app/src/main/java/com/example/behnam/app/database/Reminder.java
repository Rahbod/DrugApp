package com.example.behnam.app.database;

public class Reminder {
    private int id;
    private int drugId;
    private long start_time;
    private int count;
    private int period_time;

    public Reminder() {
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
