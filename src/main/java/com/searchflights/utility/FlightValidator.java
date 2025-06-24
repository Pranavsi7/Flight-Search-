package com.searchflights.utility;

import com.searchflights.model.Flight;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Utility class for validating {@link com.searchflights.model.Flight} objects and related flight data fields.
 * <p>
 * Provides a set of static methods to verify whether a flight object or its attributes
 * (e.g., flight class, fare, location code) meet predefined business rules.
 * </p>
 *
 * <p>
 * Validation rules include:
 * <ul>
 *     <li>Non-null and non-blank flight number, departure and arrival locations</li>
 *     <li>Fare must be a positive decimal</li>
 *     <li>Valid till date must not be in the past</li>
 *     <li>Departure time must not be null</li>
 *     <li>Flight class must be one of: Economy, Business, Premium Economy, or First Class</li>
 *     <li>Location codes must be 3 uppercase alphabetic characters</li>
 * </ul>
 * </p>
 *
 * @author Pranav Singh
 * @see com.searchflights.model.Flight
 */

public class FlightValidator {

    public static boolean isValid(Flight flight) {
        return flight != null &&
               isNotBlank(flight.getFlightNumber()) &&
               isNotBlank(flight.getDepartureLocation()) &&
               isNotBlank(flight.getArrivalLocation()) &&
               isValidFare(flight.getFare()) &&
               isValidDate(flight.getValidTill()) &&
               flight.getDepartureTime() != null;
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static boolean isValidFare(BigDecimal fare) {
        return fare != null && fare.compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean isValidDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now().minusDays(1));
    }

    public static boolean isValidFlightClass(String flightClass) {
        return flightClass != null && 
               (flightClass.equalsIgnoreCase("Economy") || 
                flightClass.equalsIgnoreCase("Business") || 
                flightClass.equalsIgnoreCase("Premium Economy") ||
                flightClass.equalsIgnoreCase("First Class"));
    }

    public static boolean isValidLocationCode(String locationCode) {
        return locationCode != null && 
               locationCode.trim().length() == 3 && 
               locationCode.matches("[A-Z]{3}");
    }
}
