package uk.ac.newcastle.enterprisemiddleware.hotel;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

@Path("hotel/bookings")
@RegisterRestClient(configKey = "hotel-api")
public interface HotelBookingService {

    /**
     *
     * @return List of hotel bookings
     */
    @GET
    @Path("/all")
    List<HotelBooking> getHotelBooking();

    /**
     *
     * @param id
     * @return hotelbooking
     */
    @GET
    @Path("/{id:[0-9]+}")
    HotelBooking getHotelById(@PathParam("id") int id);

    /**
     *
     * @param hotelBooking
     * @return response of created object
     */
    @POST
    HotelBooking createHotelBooking(HotelBooking hotelBooking);

    /**
     *
     * @param id
     * @return response
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    HotelBooking deleteHotelBooking(@PathParam("id") int id);

}