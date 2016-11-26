/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.lameduck.AirlineReservationService;
import dtu.lameduck.AirlineReservationService_Service;
import dtu.lameduck.Exception_Exception;
import dtu.lameduck.Flight;
import dtu.niceview.Hotel;
import dtu.niceview.HotelReservationService;
import dtu.niceview.HotelReservationService_Service;
import dtu.travelgood.rest.model.Itinerary;
import java.util.HashMap;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    
    @POST
    @Path("/create")
    public String createItinerary() {
        int rand = new Random().nextInt();
        Itinerary i = new Itinerary(String.valueOf(rand));
        itineraries.put(i.getID(), i);
        return i.getID();
    }
    
    @PUT
    @Path("/book/{id}/{creditcardNumber}/{creditcardName}/{expirationMonth}/{expirationYear}")
    public boolean bookItinerary(@PathParam("id") String id, 
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) throws Exception {
        if (!itineraries.containsKey(id))
            throw new Exception("Invalid ID.");
        Itinerary itinerary = itineraries.get(id);
        for (Flight f : itinerary.getFlights()) {
            airlineService.bookFlight(f.getBookingNr(), creditcardNumber, creditcardName, month, year);
        }
        for (Hotel h : itinerary.getHotels()) {
            // WS call to book hotels
            hotelService.bookHotel(h.getBookingNumber(), creditcardNumber, creditcardName, month, year, true);
        }
        itinerary.setBookedStatus();
        return true;
    }
    
    @PUT
    @Path("/cancel/{id}")
    public boolean cancelItinerary(@PathParam("id") String id,
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) throws Exception {
        if (!itineraries.containsKey(id))
            throw new Exception("Invalid ID.");
        Itinerary itinerary = itineraries.get(id);
        for (Flight f : itinerary.getFlights()) {
            airlineService.cancelFlight(f.getBookingNr(), creditcardNumber, creditcardName, month, year, f.getPrice());
        }
        for (Hotel h : itinerary.getHotels()) {
            // WS call to cancel hotels
            hotelService.cancelHotel(h.getBookingNumber());
        }
        itinerary.setCanceledStatus();
        return true;
    }
    
    private static HashMap<String, Itinerary> initMap() {
        itineraries = new HashMap<>();
        Itinerary i = new Itinerary("1224");
        itineraries.put(i.getID(), i);
        i = new Itinerary("3256");
        itineraries.put(i.getID(), i);
        i = new Itinerary("4357");
        itineraries.put(i.getID(), i);
        return itineraries;
    }

}
