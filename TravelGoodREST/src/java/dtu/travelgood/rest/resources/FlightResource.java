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
import javax.ws.rs.core.Response;
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
    public List<FlightREST> getFlightList(@PathParam("start") String start, 
            @PathParam("destination") String destination, @PathParam("date") String date) 
            throws ParseException_Exception, ParseException {
        List<FlightREST> retur = new ArrayList<>();
        List<Flight> flights = airlineService.getFlights(start, destination, date);
        for (Flight f : flights) {
            FlightREST flight = new FlightREST();
            flight.setAirline(f.getAirline());
            flight.setBookingNr(f.getBookingNr());
            flight.setCarrier(f.getCarrier());
            flight.setLanding(f.getLanding().toGregorianCalendar().getTime());
            flight.setTakeoff(f.getTakeoff().toGregorianCalendar().getTime());
            flight.setPrice(f.getPrice());
            flight.setStartAirport(f.getStartAirport());
            flight.setDestination(f.getDestination());
            retur.add(flight);
        }
        return retur;
    }
    
    @Path("{itineraryID}/{bookingNumber}/{start}/{destination}/{price}/{carrier}/{airline}/{takeoff}/{landing}")
    @PUT
    public Response addFlight(@PathParam("itineraryID") String itineraryID, 
            @PathParam("bookingNumber") int bookingNumber, @PathParam("start") String start, 
            @PathParam("destination") String destination, @PathParam("price") int price,
            @PathParam("carrier") String carrier, @PathParam("airline") String airline,
            @PathParam("takeoff") String takeoff, @PathParam("landing") String landing) throws ParseException {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid ID!").build();
        if (!ItineraryResource.itineraries.get(itineraryID).statusIsPlanning())
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        FlightREST flight = new FlightREST();
        flight.setAirline(airline);
        flight.setBookingNr(bookingNumber);
        flight.setCarrier(carrier);
        flight.setDestination(destination);
        flight.setLanding(format.parse(landing));
        flight.setPrice(price);
        flight.setStartAirport(start);
        flight.setTakeoff(format.parse(takeoff));
        FlightBooking booking = new FlightBooking(flight);
        ItineraryResource.itineraries.get(itineraryID).addFlight(booking);
        return Response.ok().build();
    }
    
    @Path("{itineraryID}/{flightID}")
    @DELETE
    public Response removeFlight(@PathParam("itineraryID") String itineraryID, @PathParam("flightID") int flightID) {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid ID!").build();
        List<FlightBooking> flights = ItineraryResource.itineraries.get(itineraryID).getFlights();
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlight().getBookingNr() == flightID) {
                flights.remove(i);
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Flight not added to itinerary").build();
    }
    
}
