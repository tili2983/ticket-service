package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import exception.TicketServiceRuntimeException;
import model.Seat;
import model.Venue;
import rater.FrontMiddleBetterRater;
import rater.Rater;

public class SimpleSeatingService {
    private int numOpenSeats;
    private int numHoldSeats;
    private int numReservedSeats;
    private int capacity;
    protected static final int OPEN = 0;
    protected static final int HOLD = 1;
    protected static final int RESERVED = 2;
    protected static final int TEMP_HOLD = -1;
    private Queue<Seat> seats;

    public SimpleSeatingService(Venue venue) {
        numOpenSeats = venue.getMaxSeats();
        numHoldSeats = 0;
        numReservedSeats = 0;
        capacity = venue.getMaxSeats();
        initSeats(venue);
    }

    private void initSeats(Venue venue) {
        int rowSize = venue.getRowSize();
        int rows = venue.getRows();
        seats = new PriorityBlockingQueue<>(rowSize * rows);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= rowSize; j++) {
                Rater rater = new FrontMiddleBetterRater();
                double rating = rater.rateSeat(j, i, venue);
                seats.offer(new Seat(j, i, rating));
            }
        }
    }

    public int getNumOpenSeats() {
        return numOpenSeats;
    }

    public int getNumHoldSeats() {
        return numHoldSeats;
    }

    public int getNumReservedSeats() {
        return numReservedSeats;
    }

    public Seat getNextBestSeat() {
        Seat seat = this.seats.poll();
        if (null != seat) {
            tempHoldSeatsForSysProcess(new ArrayList<>(Arrays.asList(seat)));
        }
        return seat;
    }

    public synchronized void holdSeats(List<Seat> seats) {
        this.processSeats(seats, HOLD);
    }

    protected synchronized void tempHoldSeatsForSysProcess(List<Seat> seats) {
        this.processSeats(seats, TEMP_HOLD);
    }

    public synchronized void reserveSeats(List<Seat> seats) {
        this.processSeats(seats, RESERVED);
    }

    public synchronized void openSeats(List<Seat> seats) {
        this.processSeats(seats, OPEN);
        seats.forEach(e->this.seats.offer(e));
    }

    /**
     * Hold seats and update counters
     * @param seats the list of seats to process
     * @throws Exception
     */
    protected synchronized void processSeats(List<Seat> seats, int operation) {
        if (null == seats || seats.isEmpty()) {
            throw new IllegalArgumentException("Seats were null or empty.");
        }

        if (operation != OPEN && operation != HOLD && operation != TEMP_HOLD && operation != RESERVED) {
            throw new IllegalArgumentException("Invalid seating operation.");
        }

        for (Seat s : seats) {
            int currentState = s.getSeatState();
            if (currentState == operation) { // already at the target state
                continue;
            }

            // Decrement counters of current state
            switch (currentState) {
                case OPEN:
                    numOpenSeats--;
                    break;
                case HOLD:
                    numHoldSeats--;
                    break;
                case RESERVED:
                    numReservedSeats--;
                    break;
                default: // Unrecognized current state.
            }

            // Increment counter and update new state
            switch (operation) {
                case OPEN:
                    s.setSeatState(OPEN);
                    numOpenSeats++;
                    break;
                case HOLD:
                    s.setSeatState(HOLD);
                    numHoldSeats++;
                    break;
                case RESERVED:
                    s.setSeatState(RESERVED);
                    numReservedSeats++;
                    break;
                case TEMP_HOLD:
                    s.setSeatState(TEMP_HOLD);
                default: // Unrecognized current state.
            }

            if (numOpenSeats < 0  || numOpenSeats > capacity ||
                    numHoldSeats < 0 || numHoldSeats > capacity ||
                    numReservedSeats < 0 || numReservedSeats > capacity) {
                throw new TicketServiceRuntimeException("Invalid seating counters.");
            }
        }
    }
}
