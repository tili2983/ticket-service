package model;

import java.sql.Timestamp;
import java.util.List;

public interface SeatHold {
    String getId();
    String getEmail();
    List<Seat> getSeats();
    int numberOfSeats();
    void setConfirmationCode(String confirmationCode);
    String getConfirmationCode();
    Timestamp getCreationTimestamp();
}
