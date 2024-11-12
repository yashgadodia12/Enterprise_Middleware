package uk.ac.newcastle.enterprisemiddleware.taxi;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Simple POJO representing TaxiBooking objects</p>
 *
 * @author Ronil
 */

public class TaxiBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private int taxiId;
    private int customerId;
    private Date bookingDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }


}