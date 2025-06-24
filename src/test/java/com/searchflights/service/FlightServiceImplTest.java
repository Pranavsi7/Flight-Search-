package com.searchflights.service;

import com.searchflights.dao.FlightDao;
import com.searchflights.model.Flight;
import com.searchflights.model.FlightSearchParameters;
import com.searchflights.model.UploadHistory;
import com.searchflights.utility.CSVImporter;
import com.searchflights.utility.FlightValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link com.searchflights.service.FlightServiceImpl}.
 * <p>
 * This test class verifies the behavior of flight-related business logic including CRUD operations,
 * search functionality, and file-based import logic.
 * </p>
 *
 * <p>
 * Dependencies are mocked using Mockito:
 * <ul>
 *     <li>{@link com.searchflights.dao.FlightDao}</li>
 *     <li>{@link com.searchflights.service.UploadHistoryService}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Tested functionality includes:
 * <ul>
 *     <li>Saving, updating, deleting, and retrieving flights</li>
 *     <li>Searching flights with parameters</li>
 *     <li>Importing flight data from CSV, including validation and duplicate checking</li>
 *     <li>Handling invalid or unsupported files during import</li>
 * </ul>
 * </p>
 *
 * <p>
 * Static utility classes {@link com.searchflights.utility.CSVImporter} and
 * {@link com.searchflights.utility.FlightValidator} are mocked using {@code MockedStatic}.
 * </p>
 *
 * @see com.searchflights.service.FlightServiceImpl
 * @see com.searchflights.model.Flight
 * @see com.searchflights.model.FlightSearchParameters
 * @see com.searchflights.utility.CSVImporter
 * @see com.searchflights.utility.FlightValidator
 * @see org.junit.jupiter.api.Test
 * 
 * @Author: Pranav Singh
 */

class FlightServiceImplTest {

    @Mock
    private FlightDao flightDao;

    @Mock
    private UploadHistoryService uploadHistoryService;

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- CRUD methods ---

    @Test
    void testSaveFlight() {
        Flight flight = new Flight();
        flight.setFlightNumber("A123");
        flightService.saveFlight(flight);
        verify(flightDao).saveFlight(flight);
        assertFalse(flight.isDirtyBit());
    }

    @Test
    void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setFlightNumber("A123");
        flightService.updateFlight(flight);
        verify(flightDao).updateFlight(flight);
        assertTrue(flight.isDirtyBit());
    }

    @Test
    void testDeleteFlight() {
        flightService.deleteFlight(1L);
        verify(flightDao).deleteFlight(1L);
    }

    @Test
    void testGetFlightById() {
        Flight flight = new Flight();
        when(flightDao.getFlightById(1L)).thenReturn(flight);
        Flight result = flightService.getFlightById(1L);
        assertEquals(flight, result);
    }

    @Test
    void testGetFlightByNumber() {
        Flight flight = new Flight();
        when(flightDao.getFlightByNumber("A123")).thenReturn(flight);
        Flight result = flightService.getFlightByNumber("A123");
        assertEquals(flight, result);
    }

    @Test
    void testGetAllFlights() {
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        when(flightDao.getAllFlights()).thenReturn(flights);
        List<Flight> result = flightService.getAllFlights();
        assertEquals(2, result.size());
    }

    @Test
    void testSearchFlights() {
        FlightSearchParameters params = new FlightSearchParameters();
        when(flightDao.searchflights(params)).thenReturn(Collections.singletonList(new Flight()));
        List<Flight> result = flightService.searchFlights(params);
        assertEquals(1, result.size());
    }

    // --- Import methods (CSV, Excel, Word, XML) ---

    @Test
    void testImportFromCSV_NullFile() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                flightService.importFromCSV(null));
        assertTrue(ex.getMessage().contains("CSV file must not be null or empty"));
    }

    @Test
    void testImportFromCSV_EmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                flightService.importFromCSV(file));
        assertTrue(ex.getMessage().contains("CSV file must not be null or empty"));
    }

    

    @Test
    void testImportFromCSV_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("flights.csv");
        when(file.getSize()).thenReturn(100L);

        // Mock CSVImporter
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        try (MockedStatic<CSVImporter> csvMock = mockStatic(CSVImporter.class)) {
            csvMock.when(() -> CSVImporter.parseCSVToFlights(file)).thenReturn(flights);

            // Mock validator and DAO
            try (MockedStatic<FlightValidator> validatorMock = mockStatic(FlightValidator.class)) {
                validatorMock.when(() -> FlightValidator.isValid(any(Flight.class))).thenReturn(true);
                when(flightDao.getFlightByNumber(anyString())).thenReturn(null);

                int saved = flightService.importFromCSV(file);

                // Two flights should be saved
                verify(flightDao, times(2)).saveFlight(any(Flight.class));
                verify(uploadHistoryService).saveUploadHistory(any(UploadHistory.class));
                assertEquals(2, saved);
            }
        }
    }

  
    @Test
    void testImportFromFile_UnsupportedFormat() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("flights.unsupported");
        Exception ex = assertThrows(Exception.class, () ->
                flightService.importFromFile(file));
        assertTrue(ex.getMessage().contains("Unsupported file format"));
    }

}
