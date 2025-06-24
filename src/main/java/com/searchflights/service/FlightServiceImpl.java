package com.searchflights.service;

import com.searchflights.dao.FlightDao;
import com.searchflights.model.Flight;
import com.searchflights.model.FlightSearchParameters;
import com.searchflights.model.UploadHistory;
import com.searchflights.utility.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
/**
 * Implementation of the {@link FlightService} interface.
 * <p>
 * Provides transactional operations for managing flights including save, update, delete,
 * search, and bulk import from various file types.
 * Includes validation, duplicate handling, and import history tracking.
 * </p>
 * 
 * <p>
 * Utilizes utility parsers and validators for converting uploaded files into
 * flight entities and storing import logs through {@code UploadHistoryService}.
 * </p>
 * 
 * @see com.searchflights.utility.CSVImporter
 * @see com.searchflights.utility.ExcelImporter
 * @see com.searchflights.utility.WordImporter
 * @see com.searchflights.utility.XMLImporter
 * @see com.searchflights.utility.FlightValidator
 * @see com.searchflights.model.Flight
 * @see com.searchflights.model.UploadHistory
 * @see com.searchflights.dao.FlightDao
 * @see UploadHistoryService
 * 
 * @author Pranav Singh
 */

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightServiceImpl.class);

    @Autowired
    private FlightDao flightDao;
    
    @Autowired
    private UploadHistoryService uploadHistoryService;

    @Override
    public void saveFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        flight.setDirtyBit(false);
        flightDao.saveFlight(flight);
        logger.info("Flight saved: {}", flight.getFlightNumber());
    }

    @Override
    public void updateFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        flight.setDirtyBit(true);
        flightDao.updateFlight(flight);
        logger.info("Flight updated: {}", flight.getFlightNumber());
    }

    @Override
    public void deleteFlight(Long id) {
        logger.info("Deleting flight with ID: {}", id);
        flightDao.deleteFlight(id);
        logger.info("Flight deleted: {}", id);
    }

    @Override
    public Flight getFlightById(Long id) {
        logger.debug("Retrieving flight with ID: {}", id);
        return flightDao.getFlightById(id);
    }

    @Override
    public Flight getFlightByNumber(String flightNumber) {
        logger.debug("Retrieving flight with number: {}", flightNumber);
        return flightDao.getFlightByNumber(flightNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        logger.debug("Fetching all flights");
        return flightDao.getAllFlights();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> searchFlights(FlightSearchParameters parameters) {
        logger.debug("Searching flights with parameters: {}", parameters);
        return flightDao.searchflights(parameters);
    }

    @Override
    public int importFromCSV(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.error("No CSV file provided for import");
            throw new IllegalArgumentException("CSV file must not be null or empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            logger.info("Importing flights from CSV file: {}", fileName);

            List<Flight> flights = CSVImporter.parseCSVToFlights(file);
            ImportResult result = saveValidFlights(flights, "CSV");
            
            UploadHistory history = new UploadHistory(
                fileName, 
                "CSV", 
                result.getSavedCount(), 
                result.getSkippedCount(), 
                file.getSize()
            );
            uploadHistoryService.saveUploadHistory(history);
            
            return result.getSavedCount();

        } catch (Exception e) {
            String errorMsg = "Failed to import CSV flights: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }

    @Override
    public int importFromExcel(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.error("No Excel file provided for import");
            throw new IllegalArgumentException("Excel file must not be null or empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            logger.info("Importing flights from Excel file: {}", fileName);

            List<Flight> flights = ExcelImporter.parseExcelToFlights(file);
            ImportResult result = saveValidFlights(flights, "Excel");
            
            
            UploadHistory history = new UploadHistory(
                fileName, 
                "Excel", 
                result.getSavedCount(), 
                result.getSkippedCount(), 
                file.getSize()
            );
            uploadHistoryService.saveUploadHistory(history);
            
            return result.getSavedCount();

        } catch (Exception e) {
            String errorMsg = "Failed to import Excel flights: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }

    @Override
    public int importFromWord(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.error("No Word file provided for import");
            throw new IllegalArgumentException("Word file must not be null or empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            logger.info("Importing flights from Word file: {}", fileName);

            List<Flight> flights = WordImporter.parseWordToFlights(file);
            ImportResult result = saveValidFlights(flights, "Word");
            
          
            UploadHistory history = new UploadHistory(
                fileName, 
                "Word", 
                result.getSavedCount(), 
                result.getSkippedCount(), 
                file.getSize()
            );
            uploadHistoryService.saveUploadHistory(history);
            
            return result.getSavedCount();

        } catch (Exception e) {
            String errorMsg = "Failed to import Word flights: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }

    @Override
    public int importFromXML(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.error("No XML file provided for import");
            throw new IllegalArgumentException("XML file must not be null or empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            logger.info("Importing flights from XML file: {}", fileName);

            List<Flight> flights = XMLImporter.parseXMLToFlights(file);
            ImportResult result = saveValidFlights(flights, "XML");
            
           
            UploadHistory history = new UploadHistory(
                fileName, 
                "XML", 
                result.getSavedCount(), 
                result.getSkippedCount(), 
                file.getSize()
            );
            uploadHistoryService.saveUploadHistory(history);
            
            return result.getSavedCount();

        } catch (Exception e) {
            String errorMsg = "Failed to import XML flights: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }

    /**
     * Helper class to hold import results
     */
    private static class ImportResult {
        private final int savedCount;
        private final int skippedCount;
        
        public ImportResult(int savedCount, int skippedCount) {
            this.savedCount = savedCount;
            this.skippedCount = skippedCount;
        }
        
        public int getSavedCount() { return savedCount; }
        public int getSkippedCount() { return skippedCount; }
    }

    /**
     * Common method to save valid flights and return import statistics
     */
    private ImportResult saveValidFlights(List<Flight> flights, String fileType) {
        if (flights == null || flights.isEmpty()) {
            logger.warn("No flights found in {} file", fileType);
            return new ImportResult(0, 0);
        }

        int savedCount = 0;
        int duplicateCount = 0;
        int invalidCount = 0;

        for (Flight flight : flights) {
            try {
                if (FlightValidator.isValid(flight)) {
                    // Check for duplicate flight numbers
                    Flight existingFlight = flightDao.getFlightByNumber(flight.getFlightNumber());
                    if (existingFlight != null) {
                        logger.warn("Duplicate flight number skipped: {}", flight.getFlightNumber());
                        duplicateCount++;
                        continue;
                    }
                    flight.setDirtyBit(false); 
                    

                    flightDao.saveFlight(flight);
                    logger.debug("Saved flight: {}", flight.getFlightNumber());
                    savedCount++;
                } else {
                    logger.warn("Invalid flight skipped: {}", flight);
                    invalidCount++;
                }
            } catch (Exception e) {
                logger.error("Error saving flight {}: {}", flight.getFlightNumber(), e.getMessage());
                invalidCount++;
            }
        }

        int totalSkipped = duplicateCount + invalidCount;
        logger.info("{} file import summary - Total: {}, Saved: {}, Duplicates: {}, Invalid: {}", 
                   fileType, flights.size(), savedCount, duplicateCount, invalidCount);

        if (savedCount == 0) {
            throw new RuntimeException("No valid flights were imported from " + fileType + " file. " +
                                     "Duplicates: " + duplicateCount + ", Invalid: " + invalidCount);
        }

        return new ImportResult(savedCount, totalSkipped);
    }

    /**
     * Legacy method - kept for backward compatibility
     */
    public int importFromFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.error("No file provided for import");
            throw new IllegalArgumentException("File must not be null or empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }

        String lowerFileName = fileName.toLowerCase();
        logger.info("Importing flights from file: {}", fileName);

        try {
            if (lowerFileName.endsWith(".csv")) {
                return importFromCSV(file);
            } else if (lowerFileName.endsWith(".xlsx") || lowerFileName.endsWith(".xls")) {
                return importFromExcel(file);
            } else if (lowerFileName.endsWith(".docx") || lowerFileName.endsWith(".doc")) {
                return importFromWord(file);
            } else if (lowerFileName.endsWith(".xml")) {
                return importFromXML(file);
            } else {
                logger.error("Unsupported file format: {}", lowerFileName);
                throw new IllegalArgumentException("Unsupported file format: " + lowerFileName + 
                                                 ". Supported formats: CSV, Excel (.xlsx/.xls), Word (.docx/.doc), XML");
            }
        } catch (Exception e) {
            String errorMsg = "Failed to import flights from " + fileName + ": " + e.getMessage();
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }
}
