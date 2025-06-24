package com.searchflights.service;

import com.searchflights.model.Flight; 
import com.searchflights.model.FlightSearchParameters;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * Service interface defining flight-related operations including CRUD functionality,
 * flight search, and bulk import from various file formats.
 * <p>
 * Supports importing flights from CSV, Excel, Word, and XML documents.
 * Used by controllers to manage business logic for flight data.
 * </p>
 * 
 * @author Pranav Singh
 */

public interface FlightService {
    void saveFlight(Flight flight);
    void updateFlight(Flight flight);
    void deleteFlight(Long id);
    Flight getFlightById(Long id);
    Flight getFlightByNumber(String flightNumber);
    List<Flight> getAllFlights();
    List<Flight> searchFlights(FlightSearchParameters parameters);

    int importFromCSV(MultipartFile file) throws Exception;
    int importFromExcel(MultipartFile file) throws Exception;
    int importFromWord(MultipartFile file) throws Exception;
    int importFromXML(MultipartFile file) throws Exception;
}
