package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.validation.ValidationException;

/**
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class InvalidNameException extends ValidationException {

    public InvalidNameException(String message) {
        super(message);
    }

    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNameException(Throwable cause) {
        super(cause);
    }
}