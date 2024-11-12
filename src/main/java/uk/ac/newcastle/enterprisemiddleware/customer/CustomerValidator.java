package uk.ac.newcastle.enterprisemiddleware.customer;

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
public class CustomerValidator {

    @Inject
    Validator validator;

    @Inject
    CustomerRepository crud;


    void validateCustomer(Customer customer) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (emailAlreadyExists(customer.getEmail(), customer.getId())) {
            throw new UniqueEmailException("Unique Email Violation");
        }

        if (!isValidCustomerName(customer.getName())) {
            throw new InvalidNameException("Invalid Customer Name");
        }

        if (!isValidPhoneNumber(customer.getPhoneNumber())) {
            throw new InvalidPhoneNumberException("Invalid Phone Number");
        }


        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

    }

    /**
     * <p>Checks if a contact with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Contact class.</p>
     *
     * <p>Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.</p>
     *
     * @param email The email to check is unique
     * @param id The user id to check the email against if it was found
     * @return boolean which represents whether the email was found, and if so if it belongs to the user with id
     */
    boolean emailAlreadyExists(String email, Long id) {
        Customer customer = null;
        Customer customerWithID = null;
        try {
            customer = crud.findAllCustomersByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (customer != null && id != null) {
            try {
                customerWithID = crud.findAllCustomersById(id);
                if (customerWithID != null && customerWithID.getEmail().equals(email)) {
                    customer = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return customer != null;
    }

    /**
     *
     * @param customerName
     * @return
     */
    boolean isValidCustomerName(String customerName) {
        return !(Objects.isNull(customerName)) && customerName.length() < 50;
    }

    boolean isValidPhoneNumber(String phoneNumber) {

        boolean isValid = false;
        if (!Objects.isNull(phoneNumber)) {
            if(phoneNumber.length() == 11 &&
                    phoneNumber.matches("\\d+") &&
                    '0' == phoneNumber.charAt(0)) {
                isValid = true;
            }
        }
        return isValid;
    }
}