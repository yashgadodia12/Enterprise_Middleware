package uk.ac.newcastle.enterprisemiddleware.flight;

import javax.validation.ValidationException;
public class UniqueDepartureandDestinationException extends ValidationException{

    public UniqueDepartureandDestinationException(String message) {
        super(message);
    }

    public UniqueDepartureandDestinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueDepartureandDestinationException(Throwable cause) {
        super(cause);
    }


}