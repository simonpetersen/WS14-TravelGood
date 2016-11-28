/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 *
 * @author Simon
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightREST {
    
    private String startAirport, destination, carrier, airline;
    private Date takeoff, landing;
    private int bookingNr, price;
    private SimpleDateFormat format;
    
    public FlightREST() {}
    
    public FlightREST(String start, String destination, String carrier, String airline,
            String takeoff, String landing, int bookingNr, int price) {
        this.format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        this.startAirport = start;
        this.destination = destination;
        this.carrier = carrier;
        this.airline = airline;
        try {
            this.takeoff = format.parse(takeoff);
            this.landing = format.parse(landing);
        } catch (ParseException ex) { System.out.println(ex.getMessage()); }
        this.bookingNr = bookingNr;
        this.price  = price;
    }

    public String getStartAirport() {
        return startAirport;
    }

    public void setStartAirport(String startAirport) {
        this.startAirport = startAirport;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Date getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(Date takeoff) {
        this.takeoff = takeoff;
    }

    public Date getLanding() {
        return landing;
    }

    public void setLanding(Date landing) {
        this.landing = landing;
    }

    public int getBookingNr() {
        return bookingNr;
    }

    public void setBookingNr(int bookingNr) {
        this.bookingNr = bookingNr;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
}
