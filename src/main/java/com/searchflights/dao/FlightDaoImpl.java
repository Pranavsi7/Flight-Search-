package com.searchflights.dao;

import com.searchflights.model.Flight;
import com.searchflights.model.FlightSearchParameters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
/**
 * The class {@link FlightDaoImpl} provides the actual implementation of the operations 
 * that interact with the data store for flight-related entities. <br>
 * <br>
 * The user of the application should not directly use this class. Instead, use the 
 * {@link com.searchflights.service.FlightServiceImpl} class for performing data store operations.
 * <br><br>
 * This class utilizes Hibernate's {@link org.hibernate.SessionFactory} for managing persistence 
 * operations and encapsulates all database interaction logic for the {@link com.searchflights.model.Flight} entity.
 * 
 * <p>Features:</p>
 * <ul>
 *     <li>CRUD operations for Flight entities</li>
 *     <li>Flight search based on search parameters</li>
 *     <li>Placeholders for file import functionality (CSV, Excel, Word, XML)</li>
 * </ul>
 * 
 * @author Pranav Singh
 * @see com.searchflights.service.FlightServiceImpl
 * @since 1.0
 */

@Repository
public class FlightDaoImpl implements FlightDao {

    private static final Logger logger = LoggerFactory.getLogger(FlightDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveFlight(Flight flight) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(flight);
        logger.info("Saved or updated flight: {}", flight.getFlightNumber());
    }

    @Override
    public void updateFlight(Flight flight) {
        Session session = sessionFactory.getCurrentSession();
        session.update(flight);
        logger.info("Updated flight: {}", flight.getFlightNumber());
    }

    @Override
    public void deleteFlight(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Flight flight = session.get(Flight.class, id);
        if (flight != null) {
            session.delete(flight);
            logger.info("Deleted flight with ID: {}", id);
        } else {
            logger.warn("Attempted to delete non-existing flight with ID: {}", id);
        }
    }

    @Override
    public Flight getFlightById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Flight flight = session.get(Flight.class, id);
        if (flight != null) {
            logger.debug("Fetched flight by ID: {}", id);
        } else {
            logger.warn("No flight found with ID: {}", id);
        }
        return flight;
    }

    @Override
    public Flight getFlightByNumber(String flightNumber) {
        Session session = sessionFactory.getCurrentSession();
        Query<Flight> query = session.createQuery(
                "FROM Flight WHERE flightNumber = :flightNumber", Flight.class);
        query.setParameter("flightNumber", flightNumber);
        Flight flight = query.uniqueResult();
        if (flight != null) {
            logger.debug("Fetched flight by number: {}", flightNumber);
        } else {
            logger.warn("No flight found with number: {}", flightNumber);
        }
        return flight;
    }

    @Override
    public List<Flight> getAllFlights() {
        Session session = sessionFactory.getCurrentSession();
        Query<Flight> query = session.createQuery("FROM Flight", Flight.class);
        List<Flight> flights = query.getResultList();
        logger.debug("Fetched all flights, count: {}", flights.size());
        return flights;
    }

    @Override
    public List<Flight> searchflights(FlightSearchParameters parameters) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "FROM Flight WHERE departureLocation = :depLoc " +
                     "AND arrivalLocation = :arrLoc " +
                     "AND date(validTill) = :searchDate " +
                     "AND flightNumber IS NOT NULL " +
                     "AND fare IS NOT NULL " +
                     "AND duration IS NOT NULL " +
                     "AND duration != 'false' " +
                     "AND duration != ''";

        if (parameters.getFlightClass() != null && !parameters.getFlightClass().isEmpty()) {
            hql += " AND flightClass = :flightClass";
        }

        Query<Flight> query = session.createQuery(hql, Flight.class);
        query.setParameter("depLoc", parameters.getDepartureLocation());
        query.setParameter("arrLoc", parameters.getArrivalLocation());
        query.setParameter("searchDate", toDate(parameters.getDepartureDate()));

        if (parameters.getFlightClass() != null && !parameters.getFlightClass().isEmpty()) {
            query.setParameter("flightClass", parameters.getFlightClass());
        }

        List<Flight> results = query.getResultList();
        logger.debug("Search completed. Parameters: {}, Results found: {}", parameters, results.size());
        return results;
    }

    
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void importFromCSV(MultipartFile file) {
        logger.info("Called importFromCSV() - not implemented");
    }

    @Override
    public void importFromExcel(MultipartFile file) {
        logger.info("Called importFromExcel() - not implemented");
    }

    @Override
    public void importFromWord(MultipartFile file) {
        logger.info("Called importFromWord() - not implemented");
    }

    @Override
    public void importFromXML(MultipartFile file) {
        logger.info("Called importFromXML() - not implemented");
    }
}

