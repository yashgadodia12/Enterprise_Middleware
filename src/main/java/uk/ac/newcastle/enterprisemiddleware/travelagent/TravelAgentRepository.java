package uk.ac.newcastle.enterprisemiddleware.travelagent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class TravelAgentRepository {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;


    /**
     * <p>Returns a List of all persisted {@link TravelAgentBooking} objects, sorted alphabetically by last name.</p>
     *
     * @return List of TravelAgentBooking objects
     */
    List<TravelAgentBooking> findAll() {
        TypedQuery<TravelAgentBooking> query = em.createNamedQuery(TravelAgentBooking.FIND_ALL, TravelAgentBooking.class);
        return query.getResultList();
    }

    /**
     *
     * @param id
     * @return TraveAgentBooking objects
     */
    public TravelAgentBooking findById(Long id) {
        return em.find(TravelAgentBooking.class, id);
    }

    /**
     * @param travelAgentBooking  object to be persisted
     * @return  object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    TravelAgentBooking create(TravelAgentBooking travelAgentBooking) throws Exception {
        em.persist(travelAgentBooking); //add to database
        return travelAgentBooking;
    }

    /**
     * <p>Deletes the provided TravelAgentBooking object from the application database if found there</p>
     *
     * @param travelAgentBooking The TravelAgentBooking object to be removed from the application database
     * @return The travelAgentBooking object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    TravelAgentBooking delete(TravelAgentBooking travelAgentBooking) throws Exception {

        if (travelAgentBooking.getId() != null) {
            em.remove(em.merge(travelAgentBooking)); //delete from database
        } else {
            log.info("TravelAgentRepository.delete() - No ID was found so can't Delete.");
        }

        return travelAgentBooking;
    }


}