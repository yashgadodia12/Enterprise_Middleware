package uk.ac.newcastle.enterprisemiddleware.booking;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.flight.Flight;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)

class BookingRestServiceIntegrationTest {
    private static Booking booking;
    private static Flight flight;
    private  static Customer customer;


    @BeforeAll
    static void setup() throws Exception {


        flight = new Flight();
        flight.setFlightNumber("M2345");
        flight.setDeparture("MUM");
        flight.setDestination("NCL");

        customer=new Customer();
        customer.setName("Testron");
        customer.setEmail("test@gmail.com");
        customer.setPhoneNumber("09890448159");


        Date date=new Date(2023,12,12);
        booking = new Booking();
        booking.setBookingDate(date);




    }
    @Test
    @Order(1)
    public void testCanCreateCustomer() {
        System.out.println(customer.toString());
        Response rep=   given().
                contentType(ContentType.JSON).
                body(customer).
                when()
                .post("/customer").
                then().
                statusCode(201)
                .extract().response();
        Long id=rep.jsonPath().getLong("id");
        customer.setId(id);
        booking.setCustomerId(customer.getId());
    }

    @Test
    @Order(2)
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
    }



    @Test
    @Order(3)
    public void testCanCreateBooking() {

        Response rep=  given().
                contentType(ContentType.JSON).
                body(booking).
                when()
                .post("/flightbooking/createBooking").
                then().
                statusCode(201)
                .extract().response();
        Long id=rep.jsonPath().getLong("id");
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setId(id);
    }

    @Test
    @Order(4)
    public void testCanGetBooking() {
        Response response = when().
                get("/flightbooking/findAllBooking").
                then().
                statusCode(200).
                extract().response();

        Booking[] result = response.body().as(Booking[].class);

        System.out.println(result[0]);

        assertEquals(1, result.length);
        assertTrue(booking.getBookingDate().equals(result[0].getBookingDate()), "Booking date check");
        assertTrue(booking.getFlightId().equals(result[0].getFlightId()), "Flight ID check");

    }

    @Test
    @Order(5)
    public void testCanDeleteBooking() {
        System.out.println(booking);
        Response response = when().
                get("/flightbooking/findAllBooking").
                then().
                statusCode(200).
                extract().response();

        Booking[] result = response.body().as(Booking[].class);

        when().
                delete("flightbooking/deleteBooking/"+result[0].getId().toString()).
                then().
                statusCode(204);
    }


}