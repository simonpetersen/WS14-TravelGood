/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtu.travelgood.rest.resources;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Simon
 */
@javax.ws.rs.ApplicationPath("TravelGood")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(dtu.travelgood.rest.resources.FlightResource.class);
        resources.add(dtu.travelgood.rest.resources.HotelResource.class);
        resources.add(dtu.travelgood.rest.resources.ItineraryResource.class);
    }
    
}
