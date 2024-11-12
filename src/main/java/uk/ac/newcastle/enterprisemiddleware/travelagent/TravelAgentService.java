package uk.ac.newcastle.enterprisemiddleware.travelagent;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
@Singleton

public class TravelAgentService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    TravelAgentRepository travelAgentRepository;

    /**
     * List all the bookings done by travel agent
     * @return travelAgentBookings
     */
    public List<TravelAgentBooking> findAll() {
        return travelAgentRepository.findAll();
    }

    /**
     *
     * @param id
     * @return Booking by id
     */
    public TravelAgentBooking findById(Long id) {
        return travelAgentRepository.findById(id);
    }

    /**
     *
     * @param booking
     * @return response of created travelAgentBooking
     * @throws Exception
     */
    public TravelAgentBooking create(TravelAgentBooking booking) throws Exception {
        return travelAgentRepository.create(booking);
    }

    /**
     *
     * @param booking
     * @return Response of operation
     * @throws Exception
     */
    public TravelAgentBooking delete(TravelAgentBooking booking) throws Exception {
        return travelAgentRepository.delete(booking);
    }

}