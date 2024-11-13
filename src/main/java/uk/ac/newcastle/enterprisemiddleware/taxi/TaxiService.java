package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.ws.rs.*;
import java.util.List;

@Path("/taxis")
@RegisterRestClient(configKey = "taxi-api")
public interface TaxiService {

    /**
     *
     * @return List of all taxis
     */

    @GET
    List<Taxi> taxi();

    /**
     *
     * @param id
     * @return taxi by id
     */
    @GET
    @Path("/{id:[0-9]+}")
    Taxi getTaxiById(@PathParam("id") long id);

    /**
     *
     * @param taxi
     * @return response of creation
     */
    @POST
    Taxi create(Taxi taxi);

    /**
     *
     * @param id
     * @return response
     */

    @DELETE
    @Path("/{id:[0-9]+}")
    Hotel delete(@PathParam("id") long id);


}