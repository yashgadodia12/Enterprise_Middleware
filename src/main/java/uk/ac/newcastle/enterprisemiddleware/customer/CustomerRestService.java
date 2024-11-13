package uk.ac.newcastle.enterprisemiddleware.customer;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
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

@Path("/customer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService service;


    /**
     * <p>Return all the Customer.</p>
     *
     * <p>The url may optionally include query parameters specifying a Customer name</p>
     *
     * @return A Response containing a list of Customer
     */
    @GET
    @Operation(summary = "Fetch all Customers", description = "Returns a JSON array of all stored Customer objects.")
    public Response retrieveAllCustomers() {
        //Create an empty collection to contain the intersection of Contacts to be returned
        List<Customer> customers=service.findAllCustomers();
        return Response.ok(customers).build();
    }

    /**
     * <p>Search for and return a Customer identified by id.</p>
     *
     * @param id The long parameter value provided as a Customer's id
     * @return A Response containing a single Customer
     */
    @GET
    @Path("/{id:[0-9]+}")
    @Operation(summary="Fetch all Customers by ID",description = "Returns a JSON array of all stored Customer objects.")
    public Response findAllCustomersById(@Parameter(description = "Id of Customer to be fetched")
                                         @Schema(minimum = "0", required = true)
                                         @PathParam("id")
                                         long id){
        Customer customer = service.findAllCustomersById(id);
        if (customer == null) {
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found Customer = " + customer);

        return Response.ok(customer).build();
    }

    /**
     * <p>Search for and return a Customer identified by email.</p>
     *
     * @param email The long parameter value provided as a Customer's email
     * @return A Response containing a single Customer
     */
    @GET
    @Path("/email/{email}")
    @Operation(summary="Fetch all Customers by email",description = "Returns a JSON array of all stored Customer objects.")
    public Response findAllCustomersByEmail(@QueryParam("email") String email){
        Customer customers;
        try {
            customers= service.findAllCustomersByEmail(email);
        } catch (NoResultException e) {
            throw new RestServiceException("No Contact with the email " + email + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(customers).build();
    }

    /**
     * <p>Creates a new customer from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param Customer The Customer object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link CustomerService#create(Customer)}
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Operation(description = "Add a new Customer to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Customer supplied in request body"),
            @APIResponse(responseCode = "409", description = "Customer supplied in request body conflicts with an existing Customer"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createCustomer(
            @Parameter(description = "JSON representation of Customer object to be added to the database", required = true)
            Customer Customer) {

        if (Customer == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            Customer.setId(null);
            service.create(Customer);
            builder = Response.status(Response.Status.CREATED).entity(Customer);

        }
        catch (ConstraintViolationException ce) {
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueEmailException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }
        catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }


        log.info("createCustomer completed. Customer = " + Customer);
        return builder.build();
    }

}
