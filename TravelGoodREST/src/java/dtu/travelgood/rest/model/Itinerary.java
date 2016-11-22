/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.model;

import dtu.lameduck.Flight;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simon
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Itinerary {
    
    private String id;
    private List<Flight> flights;
    private List<Integer> hotelBookings;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
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
    
    public void addFlight(Flight flight) 
    {
        flights.add(flight);
    }
    
    public void addHotel(int hotelBookingNumber) 
    {
        hotelBookings.add(hotelBookingNumber);
    }
    
    public List<Flight> getFlights() {
        return flights;
    }
    
    public List<Integer> getHotels() {
        return hotelBookings;
    }
    
    public void setBookedStatus() {
        status = Status.BOOKED;
    }
    
    public void setCanceledStatus() {
        status = Status.CANCELED;
    }
    
    private enum Status {
        PLANNING, BOOKED, CANCELED;
    }
}
