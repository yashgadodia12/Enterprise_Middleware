package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;


@Dependent
public class CustomerService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerValidator validator;
    @Inject
    CustomerRepository crud;

    public List<Customer> findAllCustomers(){
        return crud.findAllCustomers();
    }

    public Customer findAllCustomersById(Long id){
        return  crud.findAllCustomersById(id);
    }

    public Customer findAllCustomersByEmail(String email){
        return crud.findAllCustomersByEmail(email);
    }

    /**
     *
     * <p>Validates the data in the provided Contact object using a {@link CustomerValidator} object.<p/>
     *
     * @param customer The Customer object to be written to the database using a {@link CustomerRepository} object
     * @return The Customer object that has been successfully written to the application database
     */

    public Customer create(Customer customer) throws Exception {
        validator.validateCustomer(customer);
        try {

        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("does not exist", e);
            } else {
                throw e;
            }
        }
        return crud.createCustomer(customer);
    }

    /**
     * <p>Deletes the provided Customer object from the application database if found there.<p/>
     *
     * @param customer The Customer object to be removed from the application database
     * @return The Customer object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Customer delete(Customer customer) {
        return crud.deleteCustomer(customer);
    }
}