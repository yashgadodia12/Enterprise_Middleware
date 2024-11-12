package uk.ac.newcastle.enterprisemiddleware.flight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.smallrye.common.constraint.NotNull;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <p>This is a the Domain object. The Flight class represents how flight resources are represented in the application
 * database.</p>
 *
 * <p>The class also specifies how a Flight are retrieved from the database (with @NamedQueries), and acceptable values
 * for Flight fields (with @NotNull, @Pattern etc...)<p/>
 *
 * @author Ronil
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Flight.FIND_ALL, query = "SELECT c FROM Flight c ORDER BY c.flightNumber ASC"),
        @NamedQuery(name = Flight.FIND_BY_FLIGHT_NUMBER, query = "SELECT c FROM Flight c WHERE c.flightNumber = :flightNumber")
})
@Table(name = "Flight", uniqueConstraints = @UniqueConstraint(columnNames = "flightNumber"))
public class Flight implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Flight.findAll";
    public static final String FIND_BY_FLIGHT_NUMBER = "Flight.findByFlightNumber";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "^([A-Za-z0-9]{5})$", message = "Flight number: a non-empty alpha-numerical string which is 5 characters in length")
    @Column(name = "flightNumber")
    private String flightNumber;


    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE)
    private List<Booking> booking;

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @NotNull
    @Pattern(regexp = "^([A-Z]{3})$", message = "Flight point of departure: a non-empty alphabetical string, which is upper case and 3 characters in length")
    @Column(name = "departure")
    private String departure;

    @NotNull
    @Pattern(regexp = "^([A-Z]{3})$", message = "Flight destination: a non-empty alphabetical string, which is upper case, 3 characters in length and different from its point of departure.")
    @Column(name = "destination")
    private String destination;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}