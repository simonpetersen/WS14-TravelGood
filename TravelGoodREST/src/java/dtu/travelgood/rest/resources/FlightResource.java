/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.lameduck.AirlineReservationService;
import dtu.lameduck.AirlineReservationService_Service;
import dtu.lameduck.Flight;
import dtu.lameduck.ParseException_Exception;
import dtu.travelgood.rest.model.FlightREST;
import dtu.travelgood.rest.model.FlightBooking;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Simon
 */
@Path("flight")
public class FlightResource {
    
    private SimpleDateFormat format;
    private AirlineReservationService airlineService;
    
    public FlightResource() {
        format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        airlineService = new AirlineReservationService_Service().getAirlineReservationServicePort();
    }
    
    // http://localhost:8080/tgREST/TravelGood/flight/list?start=cph&destination=nyc
    @Path("{start}/{destination}/{date}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<dtu.travelgood.rest.model.FlightREST> getFlightList(@PathParam("start") String start, 
            @PathParam("destination") String destination, @PathParam("date") String date) 
            throws ParseException_Exception, ParseException {
        List<FlightREST> retur = new ArrayList<>();
        List<Flight> flights = airlineService.getFlights(start, destination, date);
        for (Flight f : flights) {
            FlightREST flight = new FlightREST(f.getStartAirport(), f.getDestination(), f.getCarrier(), f.getAirline(),
            format.format(f.getTakeoff().toGregorianCalendar().getTime()), 
            format.format(f.getLanding().toGregorianCalendar().getTime()), f.getBookingNr(), f.getPrice());
            retur.add(flight);
        }
        return retur;
    }
    
    @Path("{itineraryID}/{bookingNumber}/{start}/{destination}/{price}/{carrier}/{airline}/{takeoff}/{landing}")
    @PUT
    public boolean addFlight(@PathParam("itineraryID") String itineraryID, 
            @PathParam("bookingNumber") int bookingNumber, @PathParam("start") String start, 
            @PathParam("destination") String destination, @PathParam("price") int price,
            @PathParam("carrier") String carrier, @PathParam("airline") String airline,
            @PathParam("takeoff") String takeoff, @PathParam("landing") String landing) throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            throw new Exception("Invalid itinerary-ID.");
        FlightREST flight = new FlightREST(start, destination, carrier, airline, takeoff, landing, bookingNumber, price);
        FlightBooking booking = new FlightBooking(flight);
        ItineraryResource.itineraries.get(itineraryID).addFlight(booking);
        return true;
    }
    
    @Path("{itineraryID}/{flightID}")
    @DELETE
    public boolean removeFlight(@PathParam("itineraryID") String itineraryID, @PathParam("flightID") int flightID) 
            throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            throw new Exception("Invalid itinerary ID");
        List<FlightBooking> flights = ItineraryResource.itineraries.get(itineraryID).getFlights();
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlight().getBookingNr() == flightID)
                flights.remove(i);
        }
        return true;
    }
    
}
