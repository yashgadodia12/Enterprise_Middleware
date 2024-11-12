package uk.ac.newcastle.enterprisemiddleware.flight;


import javax.validation.ValidationException;


public class InvalidDepartureException extends ValidationException {

  public InvalidDepartureException(String message) {
    super(message);
  }

  public InvalidDepartureException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidDepartureException(Throwable cause) {
    super(cause);
  }
}