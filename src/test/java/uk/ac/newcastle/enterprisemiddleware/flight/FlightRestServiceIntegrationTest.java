package uk.ac.newcastle.enterprisemiddleware.flight;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(FlightRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class FlightRestServiceIntegrationTest {
    private static Flight flight;

    @BeforeAll
    static void setup() {
        flight = new Flight();
        flight.setFlightNumber("M2345");
        flight.setDeparture("MUM");
        flight.setDestination("NCL");
        System.out.println(flight.toString());

    }
    @Test
    @Order(1)
    public void testCanCreateFlight() {
        given().
                contentType(ContentType.JSON).
                body(flight).
                when()
                .post("/createFlight").
                then().
                statusCode(201);
    }

    @Test
    @Order(2)
    public void testCanGetFlight() {
        System.out.println(flight.toString());
        Response response = when().
                get("/findAllFlights").
                then().
                statusCode(200).
                extract().response();

        Flight[] result = response.body().as(Flight[].class);


        System.out.println(result[0]);

        assertEquals(1, result.length);
        assertTrue(flight.getFlightNumber().equals(result[0].getFlightNumber()), "Flight number not equal");
        assertTrue(flight.getDeparture().equals(result[0].getDeparture()), "Departure not equal");
        assertTrue(flight.getDestination().equals(result[0].getDestination()), "Destination not equal");
    }

    @Test
    @Order(3)
    public void testDuplicateFlightNumberCausesError() {
        System.out.println(flight.toString());
        given().
                contentType(ContentType.JSON).
                body(flight).
                when().
                post("/createFlight").
                then().
                statusCode(409);

    }

    @Test
    @Order(4)
    public void testCanDeleteFlight() {
        Response response = when().
                get("/findAllFlights").
                then().
                statusCode(200).
                extract().response();

        Flight[] result = response.body().as(Flight[].class);
        System.out.println(result[0].getId().toString());


    }


}