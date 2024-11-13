package uk.ac.newcastle.enterprisemiddleware.guestbooking;
import uk.ac.newcastle.enterprisemiddleware.booking.*;
import uk.ac.newcastle.enterprisemiddleware.customer.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class GuestBooking implements Serializable {

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @NotNull
    private Customer customer;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @NotNull
    private Booking booking;




}