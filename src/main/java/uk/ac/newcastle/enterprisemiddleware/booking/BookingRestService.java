package uk.ac.newcastle.enterprisemiddleware.booking;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.customer.*;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.flight.*;
import uk.ac.newcastle.enterprisemiddleware.flight.FlightService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/flightbooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    BookingService service;
    @Inject
    CustomerService customerService;

    @Inject
    FlightService flightService;


    /**
     * <p>Return all the Booking</p>
     * @return A Response containing a list of Booking
     */
    @GET
    @Path("/findAllBooking")
    @Operation(summary = "Fetch all Booking", description = "Returns a JSON array of all stored Booking objects.")
    public Response findAllBookings() {
        List<Booking> booking;
        booking = service.findAll();
        return Response.ok(booking).build();
    }


    /**
     * <p>Search for and return a Booking identified by id.</p>
     *
     * @param id The long parameter value provided as a Booking id
     * @return A Response containing a single Booking
     */
    @GET
    @Path("/findFlightByBookingId/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a Booking by id",
            description = "Returns a JSON representation of the flight object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Booking found"),
            @APIResponse(responseCode = "404", description = "Booking with id not found")
    })
    public Response findById(@Parameter(description = "Id of Booking to be fetched")
                             @Schema(minimum = "0", required = true)
                             @PathParam("id")
                             long id) {
        Booking booking = service.findById(id);
        if (booking == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found Booking = " + booking);

        return Response.ok(booking).build();
    }


    /**
     * <p>Creates a new booking from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param Booking The Booking object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link BookingService#create(Booking)}
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Path("/createBooking")
    @Operation(
            summary = "post a new booking",
            description = "posts a new booking object to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid booking object supplied in request body"),
            @APIResponse(responseCode = "409", description = "booking object already exist in databases"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createFlightBooking(@Valid Booking Booking) {
        if (Booking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Flight flight = flightService.findFlightById(Booking.getFlightId());
        Customer customer = customerService.findAllCustomersById(Booking.getCustomerId());

        if (flight == null) {
            throw new RestServiceException("No flight id " + Booking.getFlightId() +
                    " was found", Response.Status.NOT_FOUND);
        } else if (customer == null) {
            throw new RestServiceException("No customer id " + Booking.getCustomerId() +
                    " was found", Response.Status.NOT_FOUND);
        }

        Booking.setId(null);
        Booking.setFlight(flight);
        Booking.setCustomer(customer);
        try {
            service.create(Booking);
        }
        catch (UniqueBookingException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("flightId", "That flight is already used, please use a different flight and booking date");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }
        catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        }catch (Exception e) {
            log.severe(" create booking " +
                    "[" + Booking + "] error");
            e.printStackTrace();
            throw new RestServiceException(e.getMessage(), e);
        }
        return Response.ok(Booking).status(Response.Status.CREATED).build();
    }


    /**
     * <p>Deletes a booking using the ID provided. If the ID is not present then nothing can be deleted.</p>
     *
     * <p>Will return a JAX-RS response with either 204 NO CONTENT or with a map of fields, and related errors.</p>
     *
     * @param id The Long parameter value provided as the id of the Booking to be deleted
     * @return A Response indicating the outcome of the delete operation
     */

    @DELETE
    @Path("/deleteBooking/{id:[0-9]+}")
    @Operation(description = "Delete a Booking object from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The Booking has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid booking id supplied"),
            @APIResponse(responseCode = "404", description = "flight with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteBooking(
            @Parameter(description = "Booking ID to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        Booking booking = service.findById(id);
        if (booking == null) {
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.delete(booking);

            builder = Response.noContent();

        } catch (ConstraintViolationException ce) {
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        }catch (Exception e) {
            throw new RestServiceException(e);
        }
        log.info("deleteBooking completed. Booking = " + booking);
        return builder.build();
    }

}