package main.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+\\.\\d{2}");
    public static boolean isValidUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    public static boolean hasTwoDecimalPlaces(double value) {
        String stringValue = String.valueOf(value);
        Matcher matcher = DECIMAL_PATTERN.matcher(stringValue);
        return matcher.matches();
    }
    public static boolean doesDataExist(int id, Connection connection, String table_name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table_name + " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        }
    }
    public static boolean doesUUIDDataExist(String id, Connection connection, String table_name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table_name + " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        }
    }
    public static String getMarketplaceNameById(int id, Connection connection) throws SQLException {
        String sql = "SELECT marketplace_name FROM marketplace WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("marketplace_name");
            }
        }
        return null;
    }
    public static boolean isValidDateFormat(String inputDate) {
        if (inputDate == null) {return false;}
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        sdf.setLenient(false); // Disallow lenient parsing (strict mode)
        try {
            Date date = sdf.parse(inputDate);
            return inputDate.equals(sdf.format(date));
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean isIdValid(String id){
        return id != null && isValidUUID(id);
    }
    public static boolean isTitleValid(String title){ return title != null;}
    public static boolean isDescriptionValid(String description) { return description != null;}
    public static boolean isLocationIdValid(String locationId, Connection connection) throws SQLException {
        return locationId != null && doesUUIDDataExist(locationId, connection,"location");
    }
    public static boolean isListingPriceValid(double price) {
        return hasTwoDecimalPlaces(price);
    }
    public static boolean isCurrencyValid(String currency){return currency != null && currency.length() == 3;}
    public static boolean isQuantityValid(int quantity){
        return quantity > 0;
    }
    public static boolean isListingStatusValid(int listingStatusId, Connection connection) throws SQLException {
        return doesDataExist(listingStatusId, connection,"listingstatus");
    }
    public static boolean isMarketplaceValid(int marketplaceId, Connection connection) throws SQLException {
        return doesDataExist(marketplaceId, connection,"marketplace");
    }
    public static boolean isEmailAddressValid(String emailAddress) {
        return emailAddress != null && isEmailValid(emailAddress);
    }
    public static boolean isUploadTimeValid(String uploadTime) {
        return isValidDateFormat(uploadTime);
    }
}