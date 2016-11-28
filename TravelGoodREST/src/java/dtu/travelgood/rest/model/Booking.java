/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.model;

/**
 *
 * @author Simon
 */
public class Booking {
        
    private BookingStatus status;
    
    public Booking() {
        status = BookingStatus.UNCONFIRMED;
    }
    
    public void setConfirmedStatus() {
        this.status = BookingStatus.CONFIRMED;
    }
    
    public void setCancelledStatus() {
        this.status = BookingStatus.CANCELLED;
    }
    
    public void setUnconfirmedStatus() {
        this.status = BookingStatus.UNCONFIRMED;
    }
    
    public boolean statusIsConfirmed() {
        return status == BookingStatus.CONFIRMED;
    }
    
    public boolean statusIsUnconfirmed() {
        return status == BookingStatus.UNCONFIRMED;
    }
    
    public boolean statusIsCancelled() {
        return status == BookingStatus.CANCELLED;
    }
    
    private enum BookingStatus {
        CONFIRMED, UNCONFIRMED, CANCELLED;
    }
    
}
