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
public class FlightBooking extends Booking {
    
    private FlightREST flight;
    
    public FlightBooking() {}
    
    public FlightBooking(FlightREST flight) {
        super();
        this.flight = flight;
    }

    public FlightREST getFlight() {
        return flight;
    }

    public void setFlight(FlightREST flight) {
        this.flight = flight;
    }
}
