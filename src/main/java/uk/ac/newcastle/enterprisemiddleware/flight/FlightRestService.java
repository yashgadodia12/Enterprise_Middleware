package uk.ac.newcastle.enterprisemiddleware.flight;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/flight")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightService service;

    @GET
    @Path("/findAllFlights")
    @Operation(summary = "Fetch all flights", description = "Returns a JSON array of all stored flight objects.")
    public Response findAllFlights() {
        List<Flight> flight;
        flight = service.findAllFlights();
        System.out.println("Hello");

        return Response.ok(flight).build();
    }

    /**
     * <p>Search for and return a Flight identified by id.</p>
     *
     * @param id The long parameter value provided as a Flight's id
     * @return A Response containing a single Flight
     */
    @GET
    @Path("/findFlightById/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a flight by id",
            description = "Returns a JSON representation of the flight object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Flight found"),
            @APIResponse(responseCode = "404", description = "Flight with id not found")
    })
    public Response findFlightById(@Parameter(description = "Id of Flight to be fetched")
                                   @Schema(minimum = "0", required = true)
                                   @PathParam("id")
                                   long id){
        Flight flight = service.findFlightById(id);
        if (flight == null) {
            throw new RestServiceException("No Flight with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found flight = " + flight);

        return Response.ok(flight).build();
    }


    /**
     * <p>Creates a new Flight from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param flight The Flight object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link FlightService#create(Flight)}
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Path("/createFlight")
    @Operation(
            summary = "post a new flight",
            description = "posts a new flight object to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Flight created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid flight object supplied in request body"),
            @APIResponse(responseCode = "409", description = "flight object already exist in databases"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createFlight(@Parameter(description = "JSON representation of Flight object to be added to the database", required = true)
                                 Flight flight){
        if (flight == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Response.ResponseBuilder builder;
        try {
            flight.setId(null);
            service.create(flight);
            builder = Response.status(Response.Status.CREATED).entity(flight);
        }  catch (UniqueFlightNumberException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("flightNumber", "That Flight Number is already in use, please use a unique Flight Number");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch(UniqueDepartureandDestinationException e){
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("Destination", "Error check whether departure and destination are not same");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }catch (ConstraintViolationException ce) {
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        }catch (Exception e) {
            throw new RestServiceException(e);
        }

        log.info("createFlight completed. Flight = " + flight);
        return builder.build();

    }

}