package uk.ac.newcastle.enterprisemiddleware.booking;

import javax.validation.ValidationException;

public class UniqueBookingException extends ValidationException {

    public UniqueBookingException(String message) {
        super(message);
    }

    public UniqueBookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueBookingException(Throwable cause) {
        super(cause);
    }
}