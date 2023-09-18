package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportHelper {
    public static int getTotalListingCount(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(id) FROM listing";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static int getTotalListingCount(Connection connection, int year, int month) throws SQLException {
        String sql = "SELECT COUNT(id) FROM listing " +
                "WHERE YEAR(upload_time) = '" + year + "' AND MONTH(upload_time) = '" + month + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static int getTotalListingCountByMarketplace(Connection connection, String marketplace_name) throws SQLException {
        String sql = "SELECT COUNT(listing.id) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static int getTotalListingCountByMarketplace(Connection connection, String marketplace_name, int year, int month) throws SQLException {
        String sql = "SELECT COUNT(listing.id) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "' AND " +
                "YEAR(upload_time) = '" + year + "' AND MONTH(upload_time) = '" + month + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static int getTotalListingPrice(Connection connection, String marketplace_name) throws SQLException {
        String sql = "SELECT SUM(listing.listing_price) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static int getTotalListingPrice(Connection connection, String marketplace_name, int year, int month) throws SQLException {
        String sql = "SELECT SUM(listing.listing_price) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "' AND " +
                "YEAR(upload_time) = '" + year + "' AND MONTH(upload_time) = '" + month + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
    public static double getAverageListingPrice(Connection connection, String marketplace_name) throws SQLException {
        String sql = "SELECT AVG(listing.listing_price) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        }
    }

    public static double getAverageListingPrice(Connection connection, String marketplace_name, int year, int month) throws SQLException {
        String sql = "SELECT AVG(listing.listing_price) FROM listing " +
                "JOIN marketplace ON listing.marketplace = marketplace.id " +
                "WHERE marketplace.marketplace_name = '" + marketplace_name + "' AND " +
                "YEAR(upload_time) = '" + year + "' AND MONTH(upload_time) = '" + month + "'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        }
    }

    public static String getBestListerEmailAddress(Connection connection) throws SQLException {
        String sql = "SELECT owner_email_address FROM listing " +
                "GROUP BY owner_email_address " +
                "ORDER BY SUM(listing_price) DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        }
    }

    public static String getBestListerEmailAddress(Connection connection, int year, int month) throws SQLException {
        String sql = "SELECT owner_email_address FROM listing " +
                "WHERE YEAR(upload_time) = '" + year + "' AND " +
                "MONTH(upload_time) = '" + month + "'" +
                "GROUP BY owner_email_address " +
                "ORDER BY SUM(listing_price) DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        }
    }

    public static List<Integer> getYears(Connection connection) throws SQLException {
        String sql = "SELECT YEAR(upload_time) FROM listing " +
                "GROUP BY YEAR(upload_time)";
        List<Integer> years = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                years.add(resultSet.getInt(1));
            }
            return years;
        }
    }
    public static List<Integer> getMonths(Connection connection) throws SQLException {
        String sql = "SELECT MONTH(upload_time) FROM listing " +
                "GROUP BY MONTH(upload_time)";
        List<Integer> months = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                months.add(resultSet.getInt(1));
            }
            return months;
        }
    }
}