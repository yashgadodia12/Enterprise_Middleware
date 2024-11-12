package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import uk.ac.newcastle.enterprisemiddleware.booking.*;
import uk.ac.newcastle.enterprisemiddleware.customer.*;
import uk.ac.newcastle.enterprisemiddleware.flight.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;
import javax.inject.Inject;
import javax.transaction.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GuestBookingRestService {

    @Inject
    CustomerService customerService;

    @Inject
    BookingService bookingService;

    @Inject
    UserTransaction userTransaction;


    /**
     * @param guestBooking The GuestBooking object, constructed automatically from JSON input
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Operation(summary = "Addition of new booking")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Guest Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Guest Booking supplied in request body"),
            @APIResponse(responseCode = "409", description = "Booking/Customer supplied in request body conflicts with an existing Booking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })

    public Response GuestBooking(@Parameter(description = "JSON representation of Contact object to be added to the database", required = true)
                                 GuestBooking guestBooking) throws SystemException {
        if (guestBooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Response.ResponseBuilder builder;
        try {
            userTransaction.begin();
            Customer customer = guestBooking.getCustomer();
            System.out.println(customer.getEmail());
            Customer newCustomer = customerService.findAllCustomersById(customer.getId());
            System.out.println(customer.getEmail());
            if (newCustomer == null) {
                customer.setId(null);
                newCustomer = customerService.create(customer);
            }
            Booking booking = guestBooking.getBooking();
            booking.setId(null);
            booking.setCustomerId(newCustomer.getId());
            bookingService.create(booking);

            builder = Response.status(Response.Status.CREATED).entity(customer);
            userTransaction.commit();

        } catch (Exception e) {

            userTransaction.rollback();
            throw new RestServiceException(e.getMessage());
        }
        return builder.build();
    }
}
