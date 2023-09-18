package main.java;

import java.util.ArrayList;

public class MonthlyReportData extends ReportData {
    private int year;
    private int month;

    public MonthlyReportData(int year, int month, int totalListingCount, int totalEbayListingCount, int totalEbayListingPrice,
                             double averageEbayListingPrice, int totalAmazonListingCount,
                             int totalAmazonListingPrice, double averageAmazonListingPrice,
                             String bestListerEmailAddress)
    {
        super(totalListingCount, totalEbayListingCount, totalEbayListingPrice, averageEbayListingPrice,
                totalAmazonListingCount, totalAmazonListingPrice, averageAmazonListingPrice,
                bestListerEmailAddress, new ArrayList<>());
        this.year = year;
        this.month = month;
    }
}
