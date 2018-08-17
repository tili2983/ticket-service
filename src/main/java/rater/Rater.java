package rater;

import model.Seat;
import model.Venue;

public interface Rater {
    double rateSeat(int seatIndex, int rowIndex, Venue venue);
}
