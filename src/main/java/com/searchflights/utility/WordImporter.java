package com.searchflights.utility;

import com.searchflights.model.Flight;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class for importing flight data from a Word (.docx) document.
 * <p>
 * Parses a {@link MultipartFile} representing a Word document and extracts flight data
 * from the first table found. Each row (excluding the header) is converted into a
 * {@link com.searchflights.model.Flight} object.
 * </p>
 *
 * <p>
 * Expected table format (columns in order):
 * <ul>
 *     <li>Flight Number</li>
 *     <li>Departure Location</li>
 *     <li>Arrival Location</li>
 *     <li>Departure Time (HH:mm)</li>
 *     <li>Duration</li>
 *     <li>Fare (decimal)</li>
 *     <li>Valid Till (yyyy-MM-dd)</li>
 *     <li>Flight Class</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class uses Apache POI (XWPF) to read tables from Word documents.
 * Only the first table in the document is processed. If no tables are found,
 * an exception is thrown.
 * </p>
 *
 * @author Pranav Singh
 * @see com.searchflights.model.Flight
 */

public class WordImporter {
    @SuppressWarnings("resource")
	public static List<Flight> parseWordToFlights(MultipartFile file) throws Exception {
        List<Flight> flights = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);
            List<XWPFTable> tables = document.getTables();
            if (tables.isEmpty()) throw new Exception("No tables found in Word document");
            XWPFTable table = tables.get(0);
            boolean firstRow = true;
            for (XWPFTableRow row : table.getRows()) {
                if (firstRow) { firstRow = false; continue; }
                List<XWPFTableCell> cells = row.getTableCells();
                if (cells.size() < 8) continue;
                Flight flight = new Flight();
                flight.setFlightNumber(cells.get(0).getText().trim());
                flight.setDepartureLocation(cells.get(1).getText().trim());
                flight.setArrivalLocation(cells.get(2).getText().trim());
                flight.setDepartureTime(LocalTime.parse(cells.get(3).getText().trim()));
                flight.setDuration(cells.get(4).getText().trim());
                flight.setFare(new BigDecimal(cells.get(5).getText().trim()));
                flight.setValidTill(LocalDate.parse(cells.get(6).getText().trim()));
                flight.setFlightClass(cells.get(7).getText().trim());
                flights.add(flight);
            }
            document.close();
        }
        return flights;
    }
}
