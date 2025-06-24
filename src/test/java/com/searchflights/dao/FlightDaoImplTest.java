package com.searchflights.dao;

import com.searchflights.model.Flight;
import com.searchflights.model.FlightSearchParameters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link com.searchflights.dao.FlightDaoImpl}.
 * <p>
 * This class verifies the behavior of data access operations related to the {@link com.searchflights.model.Flight}
 * entity using Hibernate's {@link SessionFactory} and {@link Session}, mocked via Mockito.
 * </p>
 *
 * <p>
 * Mocked dependencies:
 * <ul>
 *     <li>{@link SessionFactory}</li>
 *     <li>{@link Session}</li>
 *     <li>{@link Query} for {@link Flight}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Covered test scenarios:
 * <ul>
 *     <li>Saving and updating flights</li>
 *     <li>Deleting existing and non-existing flights</li>
 *     <li>Fetching flights by ID and flight number</li>
 *     <li>Retrieving all flights</li>
 *     <li>Searching flights based on {@link com.searchflights.model.FlightSearchParameters}</li>
 *     <li>Stub calls to import methods (CSV, Excel, Word, XML)</li>
 * </ul>
 * </p>
 *
 * @see com.searchflights.dao.FlightDaoImpl
 * @see com.searchflights.model.Flight
 * @see com.searchflights.model.FlightSearchParameters
 * @see org.hibernate.SessionFactory
 * @see org.hibernate.Session
 * @see org.junit.jupiter.api.Test
 * 
 * @author Pranav Singh
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FlightDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Flight> query;

    @InjectMocks
    private FlightDaoImpl flightDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testSaveFlight() {
        Flight flight = new Flight();
        flight.setFlightNumber("AI101");
        flightDao.saveFlight(flight);
        verify(session).saveOrUpdate(flight);
    }

    @Test
    void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setFlightNumber("AI102");
        flightDao.updateFlight(flight);
        verify(session).update(flight);
    }

    @Test
    void testDeleteFlightExists() {
        Flight flight = new Flight();
        flight.setId(1L);
        when(session.get(Flight.class, 1L)).thenReturn(flight);
        flightDao.deleteFlight(1L);
        verify(session).delete(flight);
    }

    @Test
    void testDeleteFlightNotExists() {
        when(session.get(Flight.class, 2L)).thenReturn(null);
        flightDao.deleteFlight(2L);
        verify(session, never()).delete(any());
    }

    @Test
    void testGetFlightById() {
        Flight flight = new Flight();
        when(session.get(Flight.class, 1L)).thenReturn(flight);
        Flight result = flightDao.getFlightById(1L);
        assertEquals(flight, result);
    }

    @Test
    void testGetFlightByNumber() {
        Flight flight = new Flight();
        when(session.createQuery(anyString(), eq(Flight.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(flight);

        Flight result = flightDao.getFlightByNumber("AI101");
        assertEquals(flight, result);
    }

    @Test
    void testGetAllFlights() {
        when(session.createQuery("FROM Flight", Flight.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        List<Flight> results = flightDao.getAllFlights();
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchFlights() {
        FlightSearchParameters params = new FlightSearchParameters();
        params.setDepartureLocation("Delhi");
        params.setArrivalLocation("Mumbai");
        params.setDepartureDate(LocalDate.now());

        when(session.createQuery(anyString(), eq(Flight.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(new Flight()));

        List<Flight> results = flightDao.searchflights(params);
        assertEquals(1, results.size());
    }

    @Test
    void testImportMethods() {
        MultipartFile file = mock(MultipartFile.class);
        flightDao.importFromCSV(file);
        flightDao.importFromExcel(file);
        flightDao.importFromWord(file);
        flightDao.importFromXML(file);
        assertTrue(true);
    }
}
