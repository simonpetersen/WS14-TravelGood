/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.lameduck.AirlineReservationService;
import dtu.lameduck.AirlineReservationService_Service;
import dtu.lameduck.Exception_Exception;
import dtu.niceview.HotelReservationService;
import dtu.niceview.HotelReservationService_Service;
import dtu.travelgood.rest.model.FlightBooking;
import dtu.travelgood.rest.model.HotelBooking;
import dtu.travelgood.rest.model.Itinerary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Simon
 */
@Path("itinerary")
public class ItineraryResource {
    
    static HashMap<String, Itinerary> itineraries = initMap();
    private AirlineReservationService airlineService;
    private HotelReservationService hotelService;
    
    public ItineraryResource() {
        airlineService = new AirlineReservationService_Service().getAirlineReservationServicePort();
        hotelService = new HotelReservationService_Service().getHotelReservationServicePort();
    }
    
    @GET
    @Path("{id}")
    public Itinerary getItenerary(@PathParam("id") String id) throws Exception {
        if (itineraries.containsKey(id))
            return itineraries.get(id);
        throw new Exception("No such itinerary");
    }
    
    @GET
    public List<Itinerary> getItineraryList() {
        List<Itinerary> itins = new ArrayList<>();
        for (Itinerary i : itineraries.values()) {
            itins.add(i);
        }
        return itins;
    }
    
    @POST
    public String createItinerary() {
        int rand = Math.abs(new Random().nextInt());
        Itinerary i = new Itinerary(String.valueOf(rand));
        itineraries.put(i.getID(), i);
        return i.getID();
    }
    
    @PUT
    @Path("/book/{id}/{creditcardNumber}/{creditcardName}/{expirationMonth}/{expirationYear}")
    public Response bookItinerary(@PathParam("id") String id, 
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) {
        if (!itineraries.containsKey(id))
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid id!").build();
        Itinerary itinerary = itineraries.get(id);
        for (FlightBooking f : itinerary.getFlights()) {
            try {
                airlineService.bookFlight(f.getFlight().getBookingNr(), creditcardNumber, creditcardName, month, year);
                f.setConfirmedStatus();
            } catch (Exception_Exception ex) {
                f.setCancelledStatus();
                cancelBookings(itinerary, creditcardName, creditcardNumber, month, year);
                return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
            }
            }
        for (HotelBooking h : itinerary.getHotels()) {
            try {
                hotelService.bookHotel(h.getHotel().getBookingNumber(), creditcardNumber, creditcardName, month,
                        year, h.getHotel().isCreditCardGuaranteedRequired());
                h.setConfirmedStatus();
            } catch (dtu.niceview.Exception_Exception ex) {
                h.setCancelledStatus();
                cancelBookings(itinerary, creditcardName, creditcardNumber, month, year);
                return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
            }
        }
        itinerary.setBookedStatus();
        return Response.ok().build();
    }
    
    @DELETE
    @Path("cancel/{id}/{creditcardNumber}/{creditcardName}/{expirationMonth}/{expirationYear}")
    public Response cancelItinerary(@PathParam("id") String id,
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) throws Exception {
        if (!itineraries.containsKey(id))
            throw new Exception("Invalid ID.");
        boolean faultOccured = false;
        Itinerary itinerary = itineraries.get(id);
        for (FlightBooking f : itinerary.getFlights()) {
            if (f.statusIsUnconfirmed()) {
                airlineService.cancelFlight(f.getFlight().getBookingNr(), creditcardNumber, creditcardName, month, year, 
                    f.getFlight().getPrice());
                f.setCancelledStatus();
            } else
                faultOccured = true;
        }
        for (HotelBooking h : itinerary.getHotels()) {
            if (h.statusIsConfirmed()) {
                hotelService.cancelHotel(h.getHotel().getBookingNumber());
                h.setCancelledStatus();
            } else
                faultOccured = true;
        }
        itinerary.setCancelledStatus();
        if (faultOccured) return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                        .entity("An error occured").build();
        return Response.ok().build();
    }
    
    @POST
    @Path("reset")
    public void resetItineraries() {
        itineraries = initMap();
    }
    
    private static HashMap<String, Itinerary> initMap() {
        itineraries = new HashMap<>();
        Itinerary i = new Itinerary("122347");
        itineraries.put(i.getID(), i);
        i = new Itinerary("3256");
        itineraries.put(i.getID(), i);
        i = new Itinerary("4357");
        itineraries.put(i.getID(), i);
        return itineraries;
    }
    
    private void cancelBookings(Itinerary itinerary, String creditcardNumber, String creditcardName, int month, int year) {
        for (FlightBooking f : itinerary.getFlights()) {
            if (f.statusIsConfirmed()) {
                try {
                    airlineService.cancelFlight(f.getFlight().getBookingNr(), creditcardNumber, creditcardName, month, year,
                            f.getFlight().getPrice());
                } catch (Exception_Exception ex) {
                    Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setCancelledStatus();
            }
        }
        for (HotelBooking h : itinerary.getHotels()) {
            if (h.statusIsConfirmed()) {
                try {
                    hotelService.cancelHotel(h.getHotel().getBookingNumber());
                } catch (dtu.niceview.Exception_Exception ex) {
                    Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                }
                h.setCancelledStatus();
            } 
        }
        itinerary.setActiveStats();
    }

}
