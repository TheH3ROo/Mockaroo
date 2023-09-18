package main.java;

public class ListingObject {
    String id;
    String title;
    String description;
    String location_id;
    double listing_price;
    String currency;
    int quantity;
    int listing_status;
    int marketplace;
    String upload_time;
    String owner_email_address;

    public ListingObject(String id, String title, String description, String location_id, double listing_price,
                         String currency, int quantity, int listing_status, int marketplace,
                         String upload_time, String owner_email_address)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location_id = location_id;
        this.listing_price = listing_price;
        this.currency = currency;
        this.quantity = quantity;
        this.listing_status = listing_status;
        this.marketplace = marketplace;
        this.upload_time = upload_time;
        this.owner_email_address = owner_email_address;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation_id() {
        return location_id;
    }

    public double getListing_price() {
        return listing_price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getListing_status() {
        return listing_status;
    }

    public int getMarketplace() {
        return marketplace;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public String getOwner_email_address() {
        return owner_email_address;
    }
}