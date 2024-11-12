package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class CustomerRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Customer} objects</p>
     *
     * @return List of Customer objects
     */
    List<Customer> findAllCustomers() {
        TypedQuery<Customer> namedQuery = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return namedQuery.getResultList();
    }

    /**
     * <p>Returns a single Customer object, specified by a Long id.<p/>
     *
     * @param id The id field of the Customer to be returned
     * @return The Customer with the specified id
     */
    Customer findAllCustomersById(Long id) {
        return em.find(Customer.class, id);
    }
//
    /**
     * <p>Returns a single Customer object, specified by a String email.</p>
     *
     * <p>If there is more than one Customer with the specified email, only the first encountered will be returned.<p/>
     *
     * @param email The email field of the Customer to be returned
     * @return The first Customer with the specified email
     */
    Customer findAllCustomersByEmail(String email) {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class).setParameter("email", email);
        return query.getSingleResult();
    }
//
    /**
     * <p>Persists the provided Customer object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (ie future updates to the entity will be tracked)</p>
     *
     * <p>persist(Object) will set the @GeneratedValue @Id for an object.</p>
     *
     * @param customer The Customer object to be persisted
     * @return The Customer object that has been persisted
     */
    Customer createCustomer(Customer customer) {
        em.persist(customer);
        return customer;
    }
//
    /**
     * <p>Deletes the provided Customer object from the application database if found there</p>
     * @param customer The Customer object to be removed from the application database
     * @return The Customer object that has been successfully removed from the application database; or null
     */
    Customer deleteCustomer(Customer customer)  {
        if(customer.getId() != null && customer.getId() > 0) {
            em.remove(customer);
        } else {
            log.info("CustomerRepository.deleteById() - No ID was found so can't Delete.");
        }
        return customer;
    }

}