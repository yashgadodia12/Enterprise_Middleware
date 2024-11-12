package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.validation.ValidationException;

/**
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class InvalidPhoneNumberException extends ValidationException {

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPhoneNumberException(Throwable cause) {
        super(cause);
    }
}