package uk.ac.newcastle.enterprisemiddleware.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.smallrye.common.constraint.NotNull;
import uk.ac.newcastle.enterprisemiddleware.customer.*;
import uk.ac.newcastle.enterprisemiddleware.flight.*;
import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author yash
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Booking.FIND_ALL, query = "SELECT c FROM Booking c ORDER BY c.id ASC"),
        @NamedQuery(name = Booking.FIND_BY_ID, query = "SELECT c FROM Booking c WHERE c.id = :id"),
        @NamedQuery(name = Booking.FIND_BY_DATE_AND_FLIGHT_ID, query = "SELECT c FROM Booking c WHERE c.flightId = :flightId AND c.bookingDate = :bookingDate")
})
@XmlRootElement
@Table(name = "booking", uniqueConstraints = @UniqueConstraint(columnNames = {"flightId", "bookingDate" }))
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Booking.findAll";
    public static final String FIND_BY_ID = "Booking.findById";
    public static final String FIND_BY_DATE_AND_FLIGHT_ID ="Booking.findByDateAndFlightId" ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="flightId")
    private Long flightId;

    @NotNull
    @Column(name="customerId")
    private Long customerId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Column(name = "bookingdate")
    @Future(message ="Date Of Booking can not be in the past")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    public Date getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "flightId ", insertable = false, updatable = false)
    private Flight flight;

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(flightId, booking.flightId) && Objects.equals(customerId, booking.customerId) && Objects.equals(bookingDate, booking.bookingDate) && Objects.equals(customer, booking.customer) && Objects.equals(flight, booking.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightId, customerId, bookingDate, customer, flight);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", flightId=" + flightId +
                ", customerId=" + customerId +
                ", bookingDate=" + bookingDate +
                ", customer=" + customer +
                ", flight=" + flight +
                '}';
    }
}