/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.model;

import dtu.lameduck.Flight;
import dtu.niceview.Hotel;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simon
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Itinerary {
    
    private String id;
    private List<FlightBooking> flights;
    private List<HotelBooking> hotelBookings;
    private Status status;
    
    public Itinerary() {}
    
    public Itinerary(String id) {
        this.id = id;
        flights = new ArrayList<>();
        hotelBookings = new ArrayList<>();
        status = Status.PLANNING;
    }
    
    public String getID() {
        return id;
    }
    
    public void setID(String id) {
        this.id = id;
    }
    
    public void addFlight(FlightBooking flight) 
    {
        flights.add(flight);
    }
    
    public void addHotel(HotelBooking hotel) 
    {
        hotelBookings.add(hotel);
    }
    
    public List<FlightBooking> getFlights() {
        return flights;
    }
    
    public List<HotelBooking> getHotels() {
        return hotelBookings;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setBookedStatus() {
        this.status = Status.BOOKED;
    }
    
    public void setActiveStats() {
        this.status = Status.ACTIVE;
    }
    
    public void setCancelledStatus() {
        this.status = Status.CANCELLED;
    }
    
    public boolean statusIsPlanning() {
        return status == Status.PLANNING;
    }
    
    public boolean statusIsBooked() {
        return status == Status.BOOKED;
    }
    
    public boolean statusIsCancelled() {
        return status == Status.CANCELLED;
    }
    
    private enum Status {
        PLANNING, ACTIVE, BOOKED, CANCELLED;
    }
    
}
