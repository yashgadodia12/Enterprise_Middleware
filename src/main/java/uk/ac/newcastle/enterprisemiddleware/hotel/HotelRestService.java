package uk.ac.newcastle.enterprisemiddleware.hotel;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/hotels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class HotelRestService {

    @RestClient
    HotelService hotelService;

    /**
     *
     * @return List of all hotels created
     */
    @GET
    @Path("/hotels")
    @Operation(summary = "Fetch all hotels", description = "Returns a JSON array of all stored hotels objects.")
    public Response findAllHotels() {
        List<Hotel> hotel;
        hotel = hotelService.hotel();
        System.out.println("Hello");

        return Response.ok(hotel).build();
    }
}