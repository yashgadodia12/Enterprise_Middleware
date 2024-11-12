package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/taxis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaxiRestService {

    @RestClient
    TaxiService taxiService;

    /**
     *
     * @return List of taxis
     */
    @GET
    @Path("/taxis")
    @Operation(summary = "Fetch all hotels", description = "Returns a JSON array of all stored flight objects.")
    public Response findAllTaxi() {
        List<Taxi> taxi;
        taxi = taxiService.taxi();
        System.out.println("Hello");

        return Response.ok(taxi).build();
    }
}