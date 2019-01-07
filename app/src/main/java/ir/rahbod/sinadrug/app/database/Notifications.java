package ir.rahbod.sinadrug.app.database;

public class Notifications {
    private int id;
    private String title;
    private String message;
    private int date;
    private String jalaliDate;
    private int read;

    public Notifications() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getJalaliDate() {
        return jalaliDate;
    }

    public void setJalaliDate(String jalaliDate) {
        this.jalaliDate = jalaliDate;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
