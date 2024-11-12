package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.validation.ValidationException;

/**
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class UniqueEmailException extends ValidationException {

    public UniqueEmailException(String message) {
        super(message);
    }

    public UniqueEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueEmailException(Throwable cause) {
        super(cause);
    }
}