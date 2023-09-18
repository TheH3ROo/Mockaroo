package main.java;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.DataValidator.*;
import static main.java.WriteToFile.reportToJSON;
import static main.java.WriteToFile.writeInvalidLinesToCSV;

public class Main {
    private static final String LISTING = "-listing";
    private static final String LOCATION = "-location";
    private static final String LISTING_STATUS = "-listingStatus";
    private static final String MARKETPLACE = "-marketplace";
    private static final String CREATE_REPORTS = "-createReports";
    private static final String EBAY = "EBAY";
    private static final String AMAZON = "AMAZON";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a parameter.");
            printCommands();
            return;
        }
        String parameter = args[0];

        processParameter(parameter);
    }

    public static void processParameter(String parameter) {
        System.out.println("Processing parameter: " + parameter);
        switch (parameter) {
            case "-listing" -> getListing();
            case "-location" -> getLocation();
            case "-listingStatus" -> getListingStatus();
            case "-marketplace" -> getMarketplace();
            case "-createReports" -> createReports();
            default -> {
                System.out.println("Sorry, I did not quite catch that!");
                printCommands();
            }
        }
    }

    private static void printCommands() {
        System.out.println("Try these commands:\n" + LISTING + "\n"
                + LOCATION + "\n"
                + LISTING_STATUS + "\n"
                + MARKETPLACE + "\n"
                + CREATE_REPORTS);
    }

    private static void getListing() {
        try {
            URL url = new URL("https://my.api.mockaroo.com/listing?key=63304c70");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new Gson();
            ListingObject[] jsonList = gson.fromJson(String.valueOf(informationString), ListingObject[].class);

            String jdbcUrl = DatabaseConfig.getUrl();
            String username = DatabaseConfig.getUsername();
            String password = DatabaseConfig.getPassword();
            Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);
            List<String> invalidLines = new ArrayList<>();

            String sql = "INSERT INTO listing (id, title, description, location_id, listing_price, currency," +
                    "quantity, listing_status, marketplace, upload_time, owner_email_address) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            StringBuilder sb_invalidLines = new StringBuilder();
            StringBuilder sb_invalidRow = new StringBuilder();
            for (ListingObject object : jsonList) {
                sb_invalidLines.setLength(0);
                sb_invalidRow.setLength(0);
                if (!isIdValid(object.getId())) {
                    appendInvalidField(sb_invalidLines, "ID", object.getId());
                }
                if (!isTitleValid(object.getTitle())) {
                    appendInvalidField(sb_invalidLines, "Title", object.getTitle());
                }
                if (!isDescriptionValid(object.getDescription())) {
                    appendInvalidField(sb_invalidLines, "Description", object.getDescription());
                }
                if (!isLocationIdValid(object.getLocation_id(), dbConnection)) {
                    appendInvalidField(sb_invalidLines, "LocationId", object.getLocation_id());
                }
                if (!isListingPriceValid(object.getListing_price())) {
                    appendInvalidField(sb_invalidLines, "ListingPrice", object.getListing_price());
                }
                if (!isCurrencyValid(object.getCurrency())) {
                    appendInvalidField(sb_invalidLines, "Currency", object.getCurrency());
                }
                if (!isQuantityValid(object.getQuantity())) {
                    appendInvalidField(sb_invalidLines, "Quantity", object.getQuantity());
                }
                if (!isListingStatusValid(object.getListing_status(), dbConnection)) {
                    appendInvalidField(sb_invalidLines, "ListingStatus", object.getListing_status());
                }
                if (!isMarketplaceValid(object.getMarketplace(), dbConnection)) {
                    appendInvalidField(sb_invalidLines, "Marketplace", object.getMarketplace());
                }
                if (!isUploadTimeValid(object.getUpload_time())) {
                    appendInvalidField(sb_invalidLines, "UploadTime", object.getUpload_time());
                }
                if (!isEmailAddressValid(object.getOwner_email_address())) {
                    appendInvalidField(sb_invalidLines, "Email", object.getOwner_email_address());
                }

                sb_invalidRow.append(object.getListing_status());
                sb_invalidRow.append(";");
                sb_invalidRow.append(getMarketplaceNameById(object.getMarketplace(), dbConnection));
                sb_invalidRow.append(sb_invalidLines);
                sb_invalidRow.append(";");

                invalidLines.add(sb_invalidRow.toString());

                if (!sb_invalidLines.isEmpty()) {
                    continue;
                }
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
                preparedStatement.setString(1, object.getId());
                preparedStatement.setString(2, object.getTitle());
                preparedStatement.setString(3, object.getDescription());
                preparedStatement.setString(4, object.getLocation_id());
                preparedStatement.setDouble(5, object.getListing_price());
                preparedStatement.setString(6, object.getCurrency());
                preparedStatement.setInt(7, object.getQuantity());
                preparedStatement.setInt(8, object.getListing_status());
                preparedStatement.setInt(9, object.getMarketplace());
                java.sql.Date sqlDate = new java.sql.Date(new SimpleDateFormat("M/d/yyyy")
                        .parse(object.getUpload_time()).getTime());
                preparedStatement.setDate(10, sqlDate);
                preparedStatement.setString(11, object.getOwner_email_address());
                preparedStatement.executeUpdate();
            }
            dbConnection.close();
            writeInvalidLinesToCSV("importLog.csv", invalidLines);
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }
    private static void appendInvalidField(StringBuilder sb, String fieldName, Object value) {
        if (!sb.isEmpty()) {
            sb.append(";");
        }
        sb.append(fieldName).append(value);
    }

    private static void getLocation() {
        try {
            URL url = new URL("https://my.api.mockaroo.com/location?key=63304c70");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new Gson();
            LocationObject[] jsonList = gson.fromJson(String.valueOf(informationString), LocationObject[].class);
            String jdbcUrl = DatabaseConfig.getUrl();
            String username = DatabaseConfig.getUsername();
            String password = DatabaseConfig.getPassword();
            Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

            String sql = "INSERT INTO location (id, manager_name, phone, address_primary, address_secondary, " +
                    "country, town, postal_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            for (LocationObject object : jsonList) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
                preparedStatement.setString(1, object.getId());
                preparedStatement.setString(2, object.getManager_name());
                preparedStatement.setString(3, object.getPhone());
                preparedStatement.setString(4, object.getAddress_primary());
                preparedStatement.setString(5, object.getAddress_secondary());
                preparedStatement.setString(6, object.getCountry());
                preparedStatement.setString(7, object.getTown());
                preparedStatement.setString(8, object.getPostal_code());
                preparedStatement.executeUpdate();
            }
            dbConnection.close();
            System.out.println("The location synchronization has been successful");
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }

    private static void getListingStatus() {
        try {
            URL url = new URL("https://my.api.mockaroo.com/listingStatus?key=63304c70");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new Gson();
            ListingStatusObject[] jsonList = gson.fromJson(String.valueOf(informationString), ListingStatusObject[].class);
            String jdbcUrl = DatabaseConfig.getUrl();
            String username = DatabaseConfig.getUsername();
            String password = DatabaseConfig.getPassword();
            Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

            String sql = "INSERT INTO listingstatus (id, status_name) VALUES (?, ?)";
            for (ListingStatusObject object : jsonList) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.setString(2, object.getStatus_name());
                preparedStatement.executeUpdate();
            }
            dbConnection.close();
            System.out.println("The listingStatus synchronization has been successful");
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }

    private static void getMarketplace() {
        try {
            URL url = new URL("https://my.api.mockaroo.com/marketplace?key=63304c70");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new Gson();
            MarketplaceObject[] jsonList = gson.fromJson(String.valueOf(informationString), MarketplaceObject[].class);
            String jdbcUrl = DatabaseConfig.getUrl();
            String username = DatabaseConfig.getUsername();
            String password = DatabaseConfig.getPassword();
            Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

            String sql = "INSERT INTO marketplace (id, marketplace_name) VALUES (?, ?)";
            for (MarketplaceObject object : jsonList) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.setString(2, object.getMarketplace_name());
                preparedStatement.executeUpdate();
            }
            dbConnection.close();
            System.out.println("The marketplace synchronization has been successful");
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }

    private static void createReports() {
        var jdbcUrl = DatabaseConfig.getUrl();
        var username = DatabaseConfig.getUsername();
        var password = DatabaseConfig.getPassword();
        try {
            Connection dbConnection = DriverManager.getConnection(jdbcUrl, username, password);
            var years = ReportHelper.getYears(dbConnection);
            var months = ReportHelper.getMonths(dbConnection);
            List<ReportData> reportDataList = new ArrayList<>();
            List<MonthlyReportData> monthlyReportDataList = new ArrayList<>();
            for (Integer year: years) {
                for (Integer month : months) {
                    monthlyReportDataList.add(new MonthlyReportData(year, month,
                            ReportHelper.getTotalListingCount(dbConnection, year, month),
                            ReportHelper.getTotalListingCountByMarketplace(dbConnection, EBAY, year, month),
                            ReportHelper.getTotalListingPrice(dbConnection, EBAY, year, month),
                            ReportHelper.getAverageListingPrice(dbConnection, EBAY, year, month),
                            ReportHelper.getTotalListingCountByMarketplace(dbConnection, AMAZON, year, month),
                            ReportHelper.getTotalListingPrice(dbConnection, AMAZON, year, month),
                            ReportHelper.getAverageListingPrice(dbConnection, AMAZON, year, month),
                            ReportHelper.getBestListerEmailAddress(dbConnection, year, month)));
                }
            }
            reportDataList.add(new ReportData(
                    ReportHelper.getTotalListingCount(dbConnection),
                    ReportHelper.getTotalListingCountByMarketplace(dbConnection, EBAY),
                    ReportHelper.getTotalListingPrice(dbConnection, EBAY),
                    ReportHelper.getAverageListingPrice(dbConnection, EBAY),
                    ReportHelper.getTotalListingCountByMarketplace(dbConnection, AMAZON),
                    ReportHelper.getTotalListingPrice(dbConnection, AMAZON),
                    ReportHelper.getAverageListingPrice(dbConnection, AMAZON),
                    ReportHelper.getBestListerEmailAddress(dbConnection),
                    monthlyReportDataList));
            reportToJSON(reportDataList);
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }
}