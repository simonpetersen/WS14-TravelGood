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
public class HotelBooking {
    
    private HotelREST hotel;
    private boolean isBooked;
    
    public HotelBooking() {}
    
    public HotelBooking(HotelREST hotel) {
        this.hotel = hotel;
        this.isBooked = false;
    }

    public HotelREST getHotel() {
        return hotel;
    }

    public void setHotel(HotelREST hotel) {
        this.hotel = hotel;
    }

    public boolean isIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }
}
