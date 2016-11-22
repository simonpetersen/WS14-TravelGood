/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.lameduck.AirlineReservationService;
import dtu.lameduck.AirlineReservationService_Service;
import dtu.lameduck.Flight;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
    @Path("list")
    @GET
    public String getFlightList(@QueryParam("start") String start, 
            @QueryParam("destination") String destination, @QueryParam("date") String date) {
        //TODO: WS call
        return "Flight from "+start+" to "+destination;
    }
    
    @Path("add/{itineraryID}/{bookingNumber}/{start}/{destination}/{price}/{carrier}/{takeoff}/{landing}")
    @PUT
    public boolean addHotel(@PathParam("itineraryID") String itineraryID, 
            @PathParam("bookingNumber") int bookingNumber, @PathParam("start") String start, 
            @PathParam("destination") String destination, @PathParam("price") int price,
            @PathParam("carrier") String carrier, @PathParam("takeoff") String takeoff,
            @PathParam("landing") String landing) throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            throw new Exception("Invalid itinerary-ID.");
        Flight flight = new Flight();
        flight.setBookingNr(bookingNumber); flight.setStartAirport(start);
        flight.setDestination(destination); flight.setPrice(price);
        flight.setCarrier(carrier);
        GregorianCalendar takeoffCal = new GregorianCalendar();
        takeoffCal.setTime(format.parse(takeoff));
        GregorianCalendar landingCal = new GregorianCalendar();
        landingCal.setTime(format.parse(landing));
        XMLGregorianCalendar takoffXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(takeoffCal);
        XMLGregorianCalendar landingXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(landingCal);
        flight.setTakeoff(takoffXML); flight.setLanding(landingXML);
        ItineraryResource.itineraries.get(itineraryID).addFlight(flight);
        return true;
    }
    
}
