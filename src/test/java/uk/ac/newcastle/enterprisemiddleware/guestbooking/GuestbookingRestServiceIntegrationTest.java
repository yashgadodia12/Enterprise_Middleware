package uk.ac.newcastle.enterprisemiddleware.guestbooking;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.contact.ContactRestService;
import uk.ac.newcastle.enterprisemiddleware.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.flight.FlightRestService;
import uk.ac.newcastle.enterprisemiddleware.guestbooking.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.guestbooking.GuestBookingRestService;
import uk.ac.newcastle.enterprisemiddleware.customer.*;
import uk.ac.newcastle.enterprisemiddleware.booking.*;

import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)

public class GuestbookingRestServiceIntegrationTest {
    private static GuestBooking guestBooking;
    private static Booking booking;
    private static Customer customer;
    private static Flight flight;

    @BeforeAll
    static void setup() {
        guestBooking = new GuestBooking();

        flight = new Flight();
        flight.setFlightNumber("M2345");
        flight.setDeparture("MUM");
        flight.setDestination("NCL");

        customer=new Customer();
        customer.setName("Testron");
        customer.setEmail("test@gmail.com");
        customer.setPhoneNumber("09890448159");
        customer.setId(1L);

        booking = new Booking();
        Date date=new Date(2023,12,12);
        booking.setBookingDate(date);
        booking.setFlightId(1L);
        booking.setCustomerId(1L);





    }

    @Test
    @Order(1)
    public void testCanCreateFlight() {
        Response rep=   given().
                contentType(ContentType.JSON).
                body(flight).
                when()
                .post("/flight/createFlight").
                then().
                statusCode(201)
                .extract().response();

        Long flightId=rep.jsonPath().getLong("id");
        flight.setId(flightId);
        booking.setFlightId(flight.getId());
        guestBooking.setBooking(booking);
        customer.setId(1L);
        guestBooking.setCustomer(customer);
    }

    @Test
    @Order(2)
    public void testCanCreateGuestBooking() {
        System.out.println(customer);
        System.out.println(booking);
        System.out.println(guestBooking);
        given().
                contentType(ContentType.JSON).
                body(guestBooking).
                when()
                .post("/api").
                then().
                statusCode(201);


    }
}