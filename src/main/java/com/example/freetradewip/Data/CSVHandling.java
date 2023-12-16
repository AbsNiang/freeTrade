package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHandling {

    public static List<Activity> getActivityFromCSV(LocalDateTime lastUpdatedDateTime) {
        List<Activity> activities = new ArrayList<>();
        String csvPath = "D:/Programming/projects/freetradeWIP/activity-feed.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> rows = reader.readAll(); // maps the csv data to a list of String[]

            // gets the header names so that we can check for the correct things
            String[] columnHeaders = new String[0];
            if (!rows.isEmpty()) {
                columnHeaders = rows.get(0);
            } //TODO: exception if there is we reach else since should always have data: error with parsing
            int titleIndex = Arrays.asList(columnHeaders).indexOf("Title");
            int typeIndex = Arrays.asList(columnHeaders).indexOf("Type");
            int timestampIndex = Arrays.asList(columnHeaders).indexOf("Timestamp");
            int totalAmountIndex = Arrays.asList(columnHeaders).indexOf("Total Amount");
            int buySellIndex = Arrays.asList(columnHeaders).indexOf("Buy / Sell");
            int quantityIndex = Arrays.asList(columnHeaders).indexOf("Quantity");

            // Create a DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

            // Process each row (row contains a list of strings, which are the values for each column)
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                // Parse the string to LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(row[timestampIndex], formatter);

                // only want to record dividends or sales / purchases, and only if it hasn't been recorded yet
                // TODO: add a test to check for any new types of activity type we don't account for
                if (Activity.isTypeOfDividend(row[typeIndex]) && dateTime.isAfter(lastUpdatedDateTime)) {
                    Activity activity = new Activity(
                            row[titleIndex],
                            row[typeIndex],
                            dateTime,
                            Double.parseDouble(row[totalAmountIndex]),
                            row[buySellIndex],
                            Double.parseDouble(row[quantityIndex]));
                    activities.add(activity);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activities;
    }

    public static void main(String[] args) {
        getActivityFromCSV(LocalDateTime.MIN);
    }
}
