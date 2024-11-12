package uk.ac.newcastle.enterprisemiddleware.hotel;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

@Path("/hotels")
@RegisterRestClient(configKey = "hotel-api")
public interface HotelService {


    /**
     * @return list of hotels
     */
    @GET
    List<Hotel> hotel();

    /**
     * @param id
     * @return hotel associated to id
     */
    @GET
    @Path("/{id:[0-9]+}")
    Hotel getHotelById(@PathParam("id") long id);

    /**
     * @param hotel
     * @return response of hotel creation
     */
    @POST
    Hotel createHotel(Hotel hotel);

    /**
     * @param id
     * @return response
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    Hotel deleteHotel(@PathParam("id") long id);


}