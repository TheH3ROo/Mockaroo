package main.java;

public class ListingStatusObject {
    int id;
    String status_name;

    public ListingStatusObject(int id, String status_name) {
        this.id = id;
        this.status_name = status_name;
    }

    public int getId() {
        return id;
    }

    public String getStatus_name() {
        return status_name;
    }
}