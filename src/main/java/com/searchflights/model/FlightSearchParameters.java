package com.searchflights.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Encapsulates user-specified criteria used for searching available flights.
 * <br><br>
 * Includes departure/arrival locations, travel date, flight class, and sort preference.
 * Used primarily in service and DAO layers for filtering flights.
 * 
 * <p>Supports date formatting to align with front-end input patterns.</p>
 * 
 * @author Pranav Singh
 * @version 1.0
 * @since 2025-06-08
 */

public class FlightSearchParameters {
    private String departureLocation;
    private String arrivalLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    private String flightClass;
    private int sortPreference = 0;

    // Getters and Setters
    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public int getSortPreference() {
        return sortPreference;
    }

    public void setSortPreference(int sortPreference) {
        this.sortPreference = sortPreference;
    }
}
