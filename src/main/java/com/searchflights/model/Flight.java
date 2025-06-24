package com.searchflights.model;

import javax.persistence.*; 
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents a flight entity stored in the system. Contains detailed 
 * information such as flight number, route, timings, fare, class, and validity.
 * <br><br>
 * Mapped to the <b>flights</b> table in the database and used in various 
 * layers including DAO, service, and controller.
 * 
 * <p>Includes metadata fields like entry date and dirty bit for audit and sync tracking.</p>
 *
 * @author Pranav Singh
 * @version 1.0
 * @since 2025-06-07
 */
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    @Column(name = "departure_location", nullable = false, length = 3)
    private String departureLocation;

    @Column(name = "arrival_location", nullable = false, length = 3)
    private String arrivalLocation;

    @Column(name = "departure_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime departureTime;

    @Column(name = "duration",nullable = false, length = 10)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fare;

    @Column(name = "valid_till", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate validTill;

    @Column(name = "flight_class", nullable = false, length = 20)
    private String flightClass;
    @CreationTimestamp
    @Column(name = "date_of_entry", updatable = false)
    private LocalDateTime dateOfEntry;
    @Column(name = "dirty_bit", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean dirtyBit = false;
    public Flight() {}

    // Getters and setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public String getArrivalLocation() { return arrivalLocation; }
    public void setArrivalLocation(String arrivalLocation) { this.arrivalLocation = arrivalLocation; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public BigDecimal getFare() { return fare; }
    public void setFare(BigDecimal fare) { this.fare = fare; }
    
    public LocalDate getValidTill() { return validTill; }
    public void setValidTill(LocalDate validTill) { this.validTill = validTill; }

    public String getFlightClass() { return flightClass; }
    public void setFlightClass(String flightClass) { this.flightClass = flightClass; }
    public LocalDateTime getDateOfEntry() { return dateOfEntry; }
    public void setDateOfEntry(LocalDateTime dateOfEntry) { this.dateOfEntry = dateOfEntry; }
    public Boolean isDirtyBit() { 
        return dirtyBit; 
    } 
    public void setDirtyBit(Boolean dirtyBit) { 
        this.dirtyBit = dirtyBit; 
    }
}
