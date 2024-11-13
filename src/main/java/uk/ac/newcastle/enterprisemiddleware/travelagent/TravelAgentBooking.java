package uk.ac.newcastle.enterprisemiddleware.travelagent;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgentBooking.FIND_ALL, query = "SELECT c FROM TravelAgentBooking c ORDER BY c.id"),
        @NamedQuery(name = TravelAgentBooking.Find_BY_ID, query = "SELECT c FROM TravelAgentBooking c WHERE c.id = :id")
})
@XmlRootElement
@Table(name = "TravelAgentBooking", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class TravelAgentBooking implements Serializable {

    public static final String FIND_ALL = "TravelAgentBooking.findAll";
    public static final String Find_BY_ID = "TravelAgentBooking.findById";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hotelId")
    private int hotelId;
    @Column(name = "taxiId")
    private int taxiId;

    @Column(name = "flightId")
    private Long flightId;
    @Column(name = "bookingDate")
    private Date bookingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

}