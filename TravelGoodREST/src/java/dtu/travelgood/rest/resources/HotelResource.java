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
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

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
    
    @Path("list/{city}/{arrival}/{departure}")
    @GET
    public List<Hotel> getHotelList(@PathParam("city") String city, 
            @PathParam("arrival") String arrival, @PathParam("departure") String departure) throws ParseException_Exception {
        return hotelService.getHotels(city, arrival, departure); 
    }
    
    @Path("add/{itineraryID}/{hotelID}/{hotelAdress}/{hotelName}/{price}")
    @PUT
    public boolean addHotel(@PathParam("itineraryID") String itineraryID, 
            @PathParam("hotelID") int hotelID, @PathParam("hotelAdress") String adress,
            @PathParam("hotelName") String hotelName, @PathParam("price") int price) throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID)) {
            throw new Exception("Invalid itinerary-ID.");
        } else {
            Hotel hotel = new Hotel();
            hotel.setBookingNumber(hotelID);
            hotel.setCreditCardGuaranteedRequired(true);
            hotel.setHotelAddress(adress);
            hotel.setHotelName(hotelName);
            hotel.setPrice(price);
            ItineraryResource.itineraries.get(itineraryID).addHotel(hotel);
        }
        return true;
    }
    
    @Path("remove/{itineraryID}/{hotelID}")
    @POST
    public boolean removeHotel(@PathParam("itineraryID") String itineraryID, @PathParam("hotelID") int hotelID) 
            throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID))
            throw new Exception("Invalid itinerary ID");
        List<Hotel> hotels = ItineraryResource.itineraries.get(itineraryID).getHotels();
        for (int i = 0; i < ItineraryResource.itineraries.get(itineraryID).getHotels().size(); i++) {
            if (hotels.get(i).getBookingNumber() == hotelID)
                hotels.remove(i);
        }
        return true;
    }
    
}
