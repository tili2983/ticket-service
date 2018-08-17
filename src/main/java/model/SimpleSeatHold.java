package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class SimpleSeatHold implements SeatHold {
    /**
     * The list of seats associated with this SeatHold.
     */
    private List<Seat> seats;

    public List<Seat> getSeats() {
        return this.seats;
    }

    public int numberOfSeats() {
        return this.seats.size();
    }

    /**
     * Creation timestamp when the seat hold was made
     */
    private Timestamp creationTimestamp;

    public Timestamp getCreationTimestamp() {
        return this.creationTimestamp;
    }


    /**
     * A unique id for this seat hold.
     */
    private String seatHoldId;

    public String getId() {
        return this.seatHoldId;
    }

    /**
     * The customer's email.
     */
    private String email;

    public String getEmail() {
        return this.email;
    }

    /**
     *
     * The confirmation code of the ticket reservation
     */
    private String confirmationCode;

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getConfirmationCode() {
        return this.confirmationCode;
    }

    /**
     * Construct a new SeatBlock.
     * @param seatHoldId the unique ID for this seat hold.
     * @param email the customer's email for this seat hold.
     * @param seats the seats for this seat hold.
     */
    public SimpleSeatHold(String seatHoldId, String email, List<Seat> seats) {
        this.seatHoldId = seatHoldId;
        this.email = email;
        this.seats = seats;
        this.creationTimestamp = Timestamp.from(Instant.now());
    }
}
