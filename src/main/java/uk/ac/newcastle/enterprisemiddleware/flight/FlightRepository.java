package uk.ac.newcastle.enterprisemiddleware.flight;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;
@RequestScoped
public class FlightRepository {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Flight} objects.</p>
     *
     * @return List of Flight objects
     */
    List<Flight> findAllFlights() {
        TypedQuery<Flight> query = em.createNamedQuery(Flight.FIND_ALL, Flight.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Flight object, specified by a Long id.<p/>
     *
     * @param id The id field of the Flight to be returned
     * @return The Flight with the specified id
     */
    Flight findById(Long id) {
        return em.find(Flight.class, id);
    }

    /**
     * <p>Returns a single Flight object, specified by flight number.<p/>
     *
     * @param flightNumber The flight number field of the Flight to be returned
     * @return The Flight with the specified flight number
     */
    Flight findByFlightNumber(String flightNumber) {
        TypedQuery<Flight> query = em
                .createNamedQuery(Flight.FIND_BY_FLIGHT_NUMBER, Flight.class)
                .setParameter("flightNumber", flightNumber)
                .setMaxResults(1);
        List<Flight> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    /**
     * <p>Persists the provided Flight object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param flight The Flight object to be persisted
     * @return The Flight object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Flight create(Flight flight) {
        log.info("FlightRepository.create() - Creating " + flight.getFlightNumber());
        em.persist(flight);
        return flight;
    }

    /**
     * <p>Deletes the provided Flight object from the application database if found there</p>
     *
     * @param flight The Flight object to be removed from the application database
     * @return The Flight object that has been successfully removed from the application database; or null
     * @throws Exception
     */

    Flight delete(Flight flight) {
        log.info("FlightRepository.delete() - Deleting " + flight.getFlightNumber());
        if (flight.getId() != null) {
            em.remove(flight);
        } else {
            log.info("FlightRepository.delete() - No ID was found so can't Delete.");
        }

        return flight;
    }
}