package com.searchflights.utility;

import com.opencsv.CSVReader;
import com.searchflights.model.Flight;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class for importing flight data from a CSV file.
 * <p>
 * Parses a {@link MultipartFile} representing a CSV file and converts each row
 * (excluding the header) into a {@link com.searchflights.model.Flight} object.
 * </p>
 * <p>
 * Expected CSV format (columns in order):
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
 * @author Pranav Singh
 */

public class CSVImporter {
    public static List<Flight> parseCSVToFlights(MultipartFile file) throws Exception {
        List<Flight> flights = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean firstLine = true;
            while ((line = reader.readNext()) != null) {
                if (firstLine) { firstLine = false; continue; }
                if (line.length < 8) continue;
                Flight flight = new Flight();
                flight.setFlightNumber(line[0].trim());
                flight.setDepartureLocation(line[1].trim());
                flight.setArrivalLocation(line[2].trim());
                flight.setDepartureTime(LocalTime.parse(line[3].trim()));
                flight.setDuration(line[4].trim());
                flight.setFare(new BigDecimal(line[5].trim()));
                flight.setValidTill(LocalDate.parse(line[6].trim()));
                flight.setFlightClass(line[7].trim());
                flights.add(flight);
            }
        }
        return flights;
    }
}
