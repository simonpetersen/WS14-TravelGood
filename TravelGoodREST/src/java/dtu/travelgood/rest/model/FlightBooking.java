/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simon
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightBooking {
    
    private FlightREST flight;
    private boolean isBooked;
    
    public FlightBooking() {}
    
    public FlightBooking(FlightREST flight) {
        this.flight = flight;
        this.isBooked = false;
    }

    public FlightREST getFlight() {
        return flight;
    }

    public void setFlight(FlightREST flight) {
        this.flight = flight;
    }
    
       public boolean isIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }
}
