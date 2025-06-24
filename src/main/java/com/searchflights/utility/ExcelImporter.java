package com.searchflights.utility;

import com.searchflights.model.Flight;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class for importing flight data from an Excel (.xlsx/.xls) file.
 * <p>
 * Parses a {@link MultipartFile} representing an Excel file and converts each row
 * (excluding the header) into a {@link com.searchflights.model.Flight} object.
 * </p>
 * <p>
 * Expected Excel format (columns in order):
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
 * This class uses Apache POI to read Excel sheets and assumes the first row contains headers,
 * which are skipped during processing.
 * </p>
 *
 * @author Pranav Singh
 * @see com.searchflights.model.Flight
 */

public class ExcelImporter {
    public static List<Flight> parseExcelToFlights(MultipartFile file) throws Exception {
        List<Flight> flights = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) { firstRow = false; continue; }
                if (row.getPhysicalNumberOfCells() < 8) continue;
                Flight flight = new Flight();
                flight.setFlightNumber(getCellString(row.getCell(0)));
                flight.setDepartureLocation(getCellString(row.getCell(1)));
                flight.setArrivalLocation(getCellString(row.getCell(2)));
                flight.setDepartureTime(LocalTime.parse(getCellString(row.getCell(3))));
                flight.setDuration(getCellString(row.getCell(4)));
                flight.setFare(new BigDecimal(getCellString(row.getCell(5))));
                flight.setValidTill(LocalDate.parse(getCellString(row.getCell(6))));
                flight.setFlightClass(getCellString(row.getCell(7)));
                flights.add(flight);
            }
            workbook.close();
        }
        return flights;
    }

    private static String getCellString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }
}
