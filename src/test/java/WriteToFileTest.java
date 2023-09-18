package test.java;

import main.java.ReportData;
import main.java.WriteToFile;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WriteToFileTest {

    @Test
    void testWriteInvalidLinesToCSV() {
        List<String> invalidLines = new ArrayList<>();
        invalidLines.add("Line 1");
        invalidLines.add("Line 2");
        String fileName = "test.csv";

        WriteToFile.writeInvalidLinesToCSV(fileName, invalidLines);

        File file = new File(fileName);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void testReportToJSON() {
        List<ReportData> reportDataList = new ArrayList<>();

        WriteToFile.reportToJSON(reportDataList);

        File file = new File("report.json");
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void testReportToJSONFail() {
        List<ReportData> reportDataList = new ArrayList<>();

        WriteToFile.reportToJSON(reportDataList);

        File file = new File("report2.json");
        assertFalse(file.exists());

        file.delete();
    }
}