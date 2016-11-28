/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.lameduck.AirlineReservationService;
import dtu.lameduck.AirlineReservationService_Service;
import dtu.niceview.HotelReservationService;
import dtu.niceview.HotelReservationService_Service;
import dtu.travelgood.rest.model.FlightBooking;
import dtu.travelgood.rest.model.HotelBooking;
import dtu.travelgood.rest.model.Itinerary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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
    public boolean bookItinerary(@PathParam("id") String id, 
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) throws Exception {
        if (!itineraries.containsKey(id))
            throw new Exception("Invalid ID.");
        Itinerary itinerary = itineraries.get(id);
        for (FlightBooking f : itinerary.getFlights()) {
            airlineService.bookFlight(f.getFlight().getBookingNr(), creditcardNumber, creditcardName, month, year);
            f.setIsBooked(true);
        }
        for (HotelBooking h : itinerary.getHotels()) {
            hotelService.bookHotel(h.getHotel().getBookingNumber(), creditcardNumber, creditcardName, month, 
                    year, h.getHotel().isCreditCardGuaranteedRequired());
            h.setIsBooked(true);
        }
        return true;
    }
    
    @DELETE
    @Path("{id}")
    public boolean cancelItinerary(@PathParam("id") String id,
            @PathParam("creditcardNumber") String creditcardNumber, @PathParam("creditcardName") String creditcardName,
            @PathParam("expirationMonth") int month, @PathParam("expirationYear") int year) throws Exception {
        if (!itineraries.containsKey(id))
            throw new Exception("Invalid ID.");
        Itinerary itinerary = itineraries.get(id);
        for (FlightBooking f : itinerary.getFlights()) {
            airlineService.cancelFlight(f.getFlight().getBookingNr(), creditcardNumber, creditcardName, month, year, 
                    f.getFlight().getPrice());
            f.setIsBooked(true);
        }
        for (HotelBooking h : itinerary.getHotels()) {
            hotelService.cancelHotel(h.getHotel().getBookingNumber());
            h.setIsBooked(true);
        }
        return true;
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

}
