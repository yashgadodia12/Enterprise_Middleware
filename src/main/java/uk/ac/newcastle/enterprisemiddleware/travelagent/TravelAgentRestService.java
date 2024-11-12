package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.booking.BookingService;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.flight.FlightService;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelBookingService;
import uk.ac.newcastle.enterprisemiddleware.taxi.Taxi;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiBooking;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiBookingService;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
@Path("/travelAgent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService customerService;

    @Inject
    BookingService bookingService;

    @RestClient
    HotelBookingService hotelBookingService;
    @RestClient
    TaxiBookingService taxiBookingService;

    @Inject
    TravelAgentService travelAgentService;

    @Inject
    UserTransaction userTransaction;

    /**
     * @return A Response containing a list of TravelAgentBooking
     */
    @GET
    @Operation(summary = "Fetch all TravelAgentBooking", description = "Returns a JSON array of all stored TravelAgent objects.")
    public Response retrieveAll() {
        List<TravelAgentBooking> travelAgentBookings = travelAgentService.findAll();
        return Response.ok(travelAgentBookings).build();
    }


    /**
     * <p>Creates a new object from the values provided. Performs validation and will return a JAX-RS response with
     * @param travelAgent The tra object, constructed automatically from JSON input, to be <i>created</i> via
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Operation(description = "Add a new TravelAgentBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Travel Agent Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Agent supplied in request body"),
            @APIResponse(responseCode = "409", description = "Agent supplied in request body conflicts with an existing Contact"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    public Response createTravelAgent(
            @Parameter(description =
                    "JSON representation of TravelAgent object to be added to the database", required = true)
            TravelAgent travelAgent) throws Exception {

        if (travelAgent == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;
        try{
            userTransaction.begin();

            TravelAgentBooking travelAgentBooking = new TravelAgentBooking();

            Booking booking=travelAgent.getFlight();
            booking.setId(null);
            bookingService.create(booking);

            TaxiBooking taxiBooking;
            taxiBooking= taxiBookingService.createTaxiBooking(travelAgent.getTaxiBooking());

            HotelBooking hotelBooking;

            hotelBooking=hotelBookingService.createHotelBooking(travelAgent.getHotelBooking());


            travelAgentBooking.setId(null);
            travelAgentBooking.setFlightId(booking.getId());

            travelAgentBooking.setHotelId(hotelBooking.getId());


            travelAgentBooking.setBookingDate(travelAgent.getBookingDate());


            travelAgentBooking.setTaxiId(taxiBooking.getId());


            travelAgentBooking = travelAgentService.create(travelAgentBooking);
            builder = Response.status(Response.Status.CREATED).entity(travelAgentBooking);
            userTransaction.commit();
        } catch (Exception e) {
            userTransaction.rollback();
            e.printStackTrace();
            System.out.println(e);
            throw new RestServiceException(e);
        }
        return builder.build();
    }


    /**
     * <p>Deletes a TravelAgentBooking using the ID provided. If the ID is not present then nothing can be deleted.</p>
     *
     * <p>Will return a JAX-RS response with either 204 NO CONTENT or with a map of fields, and related errors.</p>
     *
     * @param id The Long parameter value provided as the id of the TravelAgentBooking to be deleted
     * @return A Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/deleteTravelAgentBooking/{id:[0-9]+}")
    @Operation(description = "Delete TravelAgent to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The Travel Agent has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Travel Agent id supplied"),
            @APIResponse(responseCode = "404", description = "Travel Agent with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteTravelAgent(
            @Parameter
            Long id) {
        TravelAgentBooking travelAgentBooking = travelAgentService.findById(id);
        System.out.println(travelAgentService.findById(id));
        Response.ResponseBuilder builder;

        try {

            Booking booking=bookingService.findById(travelAgentBooking.getFlightId());
//            System.out.println(booking);
            bookingService.delete(booking);
//            System.out.println("175 line");
            hotelBookingService.deleteHotelBooking(travelAgentBooking.getHotelId());

            taxiBookingService.deleteTaxiBooking(travelAgentBooking.getTaxiId());


            travelAgentService.delete(travelAgentBooking);


            builder = Response.noContent();

        } catch (ConstraintViolationException ce) {
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (Exception e) {
            throw new RestServiceException(e);
        }
        return builder.build();
    }
}