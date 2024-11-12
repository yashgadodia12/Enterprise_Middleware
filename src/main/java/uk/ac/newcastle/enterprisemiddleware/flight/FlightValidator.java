package uk.ac.newcastle.enterprisemiddleware.flight;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class FlightValidator {

    @Inject
    Validator validator;

    @Inject
    FlightRepository crud;


    void validateFlight(Flight flight) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        if(Objects.isNull(flight.getDeparture()) && flight.getDeparture().length() == 3){
            throw new InvalidDepartureException("Departure details are invalid");
        }
        if(flight.getDeparture().equalsIgnoreCase(flight.getDestination())){
            throw new UniqueDepartureandDestinationException("Departure can not be same as destination");
        }
        if (flightNumberExists(flight.getFlightNumber(), flight.getId())) {
            throw new UniqueFlightNumberException("Unique Flight Number Violation");
        }
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    boolean flightNumberExists(String flightNumber, Long id) {
        Flight Flight = null;
        Flight FlightWithID = null;
        try {
            Flight = crud.findByFlightNumber(flightNumber);
        } catch (NoResultException e) {
        }

        if (Flight != null && id != null) {
            try {
                FlightWithID = crud.findById(id);
                if (FlightWithID != null && FlightWithID.getFlightNumber().equals(flightNumber)) {
                    Flight = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return Flight != null;

    }

}