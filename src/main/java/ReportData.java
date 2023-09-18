package main.java;

import java.util.List;

public class ReportData {
    private int totalListingCount;
    private int totalEbayListingCount;
    private int totalEbayListingPrice;
    private double averageEbayListingPrice;
    private int totalAmazonListingCount;
    private int totalAmazonListingPrice;
    private double averageAmazonListingPrice;
    private String bestListerEmailAddress;
    private List<MonthlyReportData> monthlyReportDataList;

    public ReportData(int totalListingCount, int totalEbayListingCount, int totalEbayListingPrice,
                      double averageEbayListingPrice, int totalAmazonListingCount, int totalAmazonListingPrice,
                      double averageAmazonListingPrice, String bestListerEmailAddress,
                      List<MonthlyReportData> monthlyReportDataList)
    {
        this.totalListingCount = totalListingCount;
        this.totalEbayListingCount = totalEbayListingCount;
        this.totalEbayListingPrice = totalEbayListingPrice;
        this.averageEbayListingPrice = averageEbayListingPrice;
        this.totalAmazonListingCount = totalAmazonListingCount;
        this.totalAmazonListingPrice = totalAmazonListingPrice;
        this.averageAmazonListingPrice = averageAmazonListingPrice;
        this.bestListerEmailAddress = bestListerEmailAddress;
        this.monthlyReportDataList = monthlyReportDataList;
    }
}