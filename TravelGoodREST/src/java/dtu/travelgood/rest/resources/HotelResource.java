/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import javax.ws.rs.GET;
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
    
    @Path("list/{city}/{arrival}/{departure}")
    @GET
    public String getHotelList(@PathParam("city") String city, 
            @PathParam("arrival") String arrival, @PathParam("departure") String departure) {
        //TODO: WS call
        return "Hotel from "+arrival+" to "+departure+" in "+city;
    }
    
    @Path("add/{itineraryID}/{hotelID}")
    @PUT
    public boolean addHotel(@PathParam("itineraryID") String itineraryID, 
            @PathParam("hotelID") int hotelID) throws Exception {
        if (!ItineraryResource.itineraries.containsKey(itineraryID)) {
            throw new Exception("Invalid itinerary-ID.");
        } else
            ItineraryResource.itineraries.get(itineraryID).addHotel(hotelID);
        return true;
    }
    
}
