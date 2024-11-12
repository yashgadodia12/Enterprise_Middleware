package uk.ac.newcastle.enterprisemiddleware.travelagent;

import io.smallrye.common.constraint.NotNull;
import uk.ac.newcastle.enterprisemiddleware.booking.*;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.flight.*;
import uk.ac.newcastle.enterprisemiddleware.hotel.*;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiBooking;

import java.io.Serializable;
import java.util.Date;

public class TravelAgent implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Booking flight;
    @NotNull
    private HotelBooking hotelBooking;
    @NotNull
    private TaxiBooking taxiBooking;
    @NotNull
    private Date bookingDate;

    public Booking getFlight() {
        return flight;
    }

    public void setFlight(Booking flight) {
        this.flight = flight;
    }

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public TaxiBooking getTaxiBooking() {
        return taxiBooking;
    }

    public void setTaxiBooking(TaxiBooking taxiBooking) {
        this.taxiBooking = taxiBooking;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

}