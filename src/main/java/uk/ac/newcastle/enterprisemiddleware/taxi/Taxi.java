package uk.ac.newcastle.enterprisemiddleware.taxi;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * <p>Simple POJO representing Taxi objects</p>
 *
 * @author Ronil
 */
public class Taxi implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @NotNull
    private String registration;


    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @NotNull
    private int seats;

}