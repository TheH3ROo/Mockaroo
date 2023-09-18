package main.java;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;
import org.apache.commons.net.ftp.FTPClient;

public class WriteToFile {
    public static void writeInvalidLinesToCSV(String fileName, List<String> invalidLines) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {
            for (String line : invalidLines) {
                csvWriter.writeNext(new String[] { line });
            }
            System.out.println("Invalid lines have been written to " + fileName);
        } catch (IOException e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }
    public static void reportToJSON(List<ReportData> reportDataList){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("report.json")) {
            gson.toJson(reportDataList, writer);
            writeToFTP();
            System.out.println("JSON report file created successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void writeToFTP() {
        try {
            FTPClient ftpClient = new FTPClient();
            final String server = FTPConfig.getUrl();
            final String username = FTPConfig.getUsername();
            final String password = FTPConfig.getPassword();
            final int port = 21;

            ftpClient.connect(server, port);
            ftpClient.login(username, password);

            ftpClient.storeFile("report.json", new FileInputStream("report.json"));

            ftpClient.disconnect();
            System.out.println("JSON report file stored to FTP successfully.");
        }catch (Exception e){
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }
}