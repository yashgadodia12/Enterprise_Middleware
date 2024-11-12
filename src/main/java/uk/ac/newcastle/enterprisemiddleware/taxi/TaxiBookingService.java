package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import java.util.List;

@Path("/bookings")
@RegisterRestClient(configKey = "taxi-api")
public interface TaxiBookingService {

    /**
     *
     * @return list of taxi booking
     */
    @GET
    List<TaxiBooking> getTaxiBooking();

    /**
     *
     * @param id
     * @return taxibooking associated to id
     */
    @GET
    @Path("/{id:[0-9]+}")
    TaxiBooking getTaxiById(@PathParam("id") long id);

    /**
     *
     * @param taxiBooking
     * @return response of creation of taxi booking
     */
    @POST
    TaxiBooking createTaxiBooking(TaxiBooking taxiBooking);

    /**
     *
     * @param id
     * @return response
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    TaxiBooking deleteTaxiBooking(@PathParam("id") long id);

}