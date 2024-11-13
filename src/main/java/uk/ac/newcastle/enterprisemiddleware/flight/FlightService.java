package uk.ac.newcastle.enterprisemiddleware.flight;

import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
@Dependent
public class FlightService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightValidator validator;

    @Inject
    FlightRepository crud;

    /**
     * <p>Returns a List of all persisted {@link Flight} objects.<p/>
     *
     * @return List of Flight objects
     */
    public List<Flight> findAllFlights() {
        return crud.findAllFlights();
    }

    /**
     * <p>Returns a List of all persisted {@link Flight} objects.<p/>
     *
     * @param  id
     * @return List of Flight objects
     */
    public Flight findFlightById(long id){
        return crud.findById(id);
    }

    public Flight findByFlightNumber(String flightNumber) {
        return crud.findByFlightNumber(flightNumber);
    }

    /**
     * <p>Writes the provided Flight object to the application database.<p/>
     *
     * <p>Validates the data in the provided Flight object using a {@link FlightValidator} object.<p/>
     *
     * @param flight The Flight object to be written to the database using a {@link FlightRepository} object
     * @return The flight object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Flight create(Flight flight) throws Exception{
        validator.validateFlight(flight);
        try {
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("does not exist", e);
            } else {
                throw e;
            }
        }
        return crud.create(flight);

    }
    /**
     * <p>Deletes the provided Flight object from the application database if found there.<p/>
     *
     * @param flight The Flight object to be removed from the application database
     * @return The Flight object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Flight delete(Flight flight) {
        log.info("delete() - Deleting " + flight.toString());

        Flight deletedFlight = null;

        if (flight.getId() != null) {
            deletedFlight = crud.delete(flight);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }
        return deletedFlight;
    }


}