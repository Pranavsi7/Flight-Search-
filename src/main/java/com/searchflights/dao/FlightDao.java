package com.searchflights.dao;

import com.searchflights.model.Flight;
import com.searchflights.model.FlightSearchParameters;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
/**
 * The {@link FlightDao} interface defines the contract for data access operations 
 * related to the {@link com.searchflights.model.Flight} entity.
 * <br><br>
 * It is intended to be used via its implementation, such as {@link com.searchflights.dao.FlightDaoImpl},
 * and not accessed directly by application layers.
 *
 * <p>Includes methods for CRUD operations, flight search, and file import placeholders.</p>
 *
 * @author Pranav Singh
 * @since 1.0
 */

public interface FlightDao {
    void saveFlight(Flight flight);
    void updateFlight(Flight flight);
    void deleteFlight(Long id);
    Flight getFlightById(Long id);
    Flight getFlightByNumber(String flightNumber);
    List<Flight> getAllFlights();
    List<Flight> searchflights(FlightSearchParameters parameters);
    void importFromCSV(MultipartFile file);
    void importFromExcel(MultipartFile file);
    void importFromWord(MultipartFile file);
    void importFromXML(MultipartFile file);
}