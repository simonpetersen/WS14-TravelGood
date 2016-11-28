/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import dtu.niceview.Hotel;
import dtu.niceview.HotelReservationService;
import dtu.niceview.HotelReservationService_Service;
import dtu.niceview.ParseException_Exception;
import dtu.travelgood.rest.model.HotelREST;
import dtu.travelgood.rest.model.HotelBooking;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Simon
 */
@Path("hotel")
public class HotelResource {
    
    private HotelReservationService hotelService;
    
    public HotelResource() {
        hotelService = new HotelReservationService_Service().getHotelReservationServicePort();
    }
    
    @Path("{city}/{arrival}/{departure}")
    @GET
    public List<HotelREST> getHotelList(@PathParam("city") String city, 
            @PathParam("arrival") String arrival, @PathParam("departure") String departure) throws ParseException_Exception {
        List<Hotel> hotels = hotelService.getHotels(city, arrival, departure);
        List<HotelREST> retur = new ArrayList<>();
        for (Hotel h : hotels) {
            HotelREST hotel = new HotelREST(h.getHotelName(), h.getHotelAddress(), h.getBookingNumber(), h.getPriceOfOneNight(), 
                    h.isCreditCardGuaranteedRequired());
            hotel.setPriceTotal(h.getPriceTotal());
            retur.add(hotel);
        }
        return retur; 
    }
    
    @Path("{itineraryID}/{hotelID}/{hotelAdress}/{hotelName}/{price}/{creditCardGuarantee}")
    @PUT
    public Response addHotel(@PathParam("itineraryID") String itineraryID, 
            @PathParam("hotelID") int hotelID, @PathParam("hotelAdress") String adress,
            @PathParam("hotelName") String hotelName, @PathParam("price") int price,
            @PathParam("creditCardGuarantee") boolean creditcardGuarantee) throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid ID").build();
        if (!ItineraryResource.itineraries.get(itineraryID).statusIsPlanning())
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("Not possible to change itinerary").build();
        HotelREST hotel = new HotelREST(hotelName, adress, hotelID, price, creditcardGuarantee);
        HotelBooking booking = new HotelBooking(hotel);
        ItineraryResource.itineraries.get(itineraryID).addHotel(booking);
        return Response.ok().build();
    }
    
    @Path("{itineraryID}/{hotelID}")
    @DELETE
    public Response removeHotel(@PathParam("itineraryID") String itineraryID, @PathParam("hotelID") int hotelID) {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            return Response.status(Response.Status.NOT_FOUND).entity("Invalid ID").build();
        List<HotelBooking> hotels = ItineraryResource.itineraries.get(itineraryID).getHotels();
        for (int i = 0; i < ItineraryResource.itineraries.get(itineraryID).getHotels().size(); i++) {
            if (hotels.get(i).getHotel().getBookingNumber() == hotelID) {
                hotels.remove(i);
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Hotel not added to itinerary").build();
    }
    
}
