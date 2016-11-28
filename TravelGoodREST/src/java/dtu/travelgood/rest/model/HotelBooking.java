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
public class HotelBooking extends Booking {
    
    private HotelREST hotel;
    
    public HotelBooking() {}
    
    public HotelBooking(HotelREST hotel) {
        super();
        this.hotel = hotel;
    }

    public HotelREST getHotel() {
        return hotel;
    }

    public void setHotel(HotelREST hotel) {
        this.hotel = hotel;
    }
}
