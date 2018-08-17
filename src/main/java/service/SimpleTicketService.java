package service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import exception.TicketServiceRuntimeException;
import generator.Generator;
import generator.ReservationConfirmationIdGenerator;
import generator.SeatHoldIdGenerator;
import model.Seat;
import model.SeatHold;
import model.SimpleSeatHold;
import model.Venue;
import org.apache.log4j.Logger;
import util.EmailValidator;

public class SimpleTicketService implements TicketService {
    private static Logger logger = Logger.getLogger(SimpleTicketService.class);
    private SimpleSeatingService seatingService;
    private Map<String, SeatHold> seatHoldMap;
    private Map<String, SeatHold> confirmationMap;
    private static Generator seatHoldIdGenerator;
    private static Generator reservationConfirmationGenerator;
    private final long holdLimit;
    private static final long DEFAULT_HOLD_EXPIRATION_TIME = 60_000L;

    public SimpleTicketService(Venue venue, long holdExpirationTime) throws TicketServiceRuntimeException {
        if (null == venue) {
            throw new TicketServiceRuntimeException("The provided venue is null.");
        }

        this.seatingService = new SimpleSeatingService(venue);
        this.holdLimit = (holdExpirationTime >= 0) ? holdExpirationTime : DEFAULT_HOLD_EXPIRATION_TIME;

        seatHoldMap = new HashMap<>();
        confirmationMap = new HashMap<>();
        seatHoldIdGenerator = new SeatHoldIdGenerator();
        reservationConfirmationGenerator = new ReservationConfirmationIdGenerator();
    }

    /**
     * Find and hold the best available seats for a customer.
     * Synchronizing leads to performance bottleneck such that only one thread can find seats and mark them as held at
     * a time. This is to avoid multiple threads retrieving and marking the same seats resulting in multiple seats being
     * held by multiple customers.
     * Use findAndHoldSeatsWrap() instead!
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer related information
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if (!EmailValidator.isVaild(customerEmail)) {
            logger.error("Invalid email address.");
            return null;
        }

        if (numSeatsAvailable() < numSeats) {
            return null; // Insufficient seats
        }

        // Hold seats
        List<Seat> seats = new LinkedList<>();
        for (int i = 0; i < numSeats; i++) {
            Seat s = seatingService.getNextBestSeat();
            if (null == s) {
                logger.error("Collision. Already checked for availability of the seats but still not available.");
                cleanUpSeats(seats);
                return null;
            }
            seats.add(s);
        }

        holdSeats(seats);

        // Create seatHold
        SeatHold seatHold;
        try {
            seatHold = new SimpleSeatHold(seatHoldIdGenerator.generateId(), customerEmail, seats);
        } catch (TicketServiceRuntimeException e) {
            // this should never happen.
            logger.error("Failed to seatHold.");
            cleanUpSeats(seats);
            return null;
        }

        return seatHold;
    }

    /**
     * Hold seats for a specific customer identified by validate email address.
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(String seatHoldId, String customerEmail) {
        if (!EmailValidator.isVaild(customerEmail)) {
            logger.error("Invalid email address.");
            return null;
        }

        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        if (null == seatHold) {
            logger.error("The given seat hold is not found.");
            return null;
        }

        if (!seatHold.getEmail().equals(customerEmail)) {
            logger.error("Incorrect email address for the seat hold");
            return null;
        }

        try {
            seatingService.reserveSeats(seatHold.getSeats());
        } catch (Exception e) {
            logger.error("Failed to reserve seats on hold.");
            holdSeats(seatHold.getSeats());
        }

        String newConfirmationCode = reservationConfirmationGenerator.generateId();
        seatHold.setConfirmationCode(newConfirmationCode);
        confirmationMap.put(newConfirmationCode, seatHold);
        seatHoldMap.remove(seatHoldId, seatHold); // only remove if seatHold matches
        return newConfirmationCode;
    }

    private void cleanUpSeats(List<Seat> seats) {
        try {
            seatingService.openSeats(seats);
        } catch (Exception e) {
            logger.error("Failed to reset seats to OPEN state.");
        }
    }

    private void holdSeats(List<Seat> seats) {
        try {
            seatingService.holdSeats(seats);
        } catch (Exception e) {
            logger.error("Failed to hold seats.");
            cleanUpSeats(seats);
            // Need to notify caller service.
        }
    }

    /**
     * The number of seats in the venue that are neither held nor reserved.
     * Race condition in count may be acceptable in some cases.
     * @return the number of tickets/seats available in the venue
     */
    public int numSeatsAvailable() {
        return seatingService.getNumOpenSeats();
    }

    protected int numSeatsReserved() {
        return seatingService.getNumReservedSeats();
    }

    protected int numSeatsOnHold() {
        return seatingService.getNumHoldSeats();
    }

    /**
     * Removes expired seat holds.
     * Periodic job from the parent service to clean up expired seat holds upon expiration.
     */
    public void removeExpiredSeatHolds() {
        Timestamp cur = Timestamp.from(Instant.now());
        Iterator<Map.Entry<String, SeatHold>> iterator = seatHoldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SeatHold> e = iterator.next();
            SeatHold seatHold = e.getValue();
            long duration = cur.getTime() - seatHold.getCreationTimestamp().getTime();
            if (duration >= holdLimit) {
                cleanUpSeats(seatHold.getSeats());
                iterator.remove();
                seatHoldMap.remove(seatHold.getId(), seatHold);
            }
        }
    }

    /**
     * Caller service should use this method to find and hold seats for the given customer.
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer related information
     * @return seatHold id or null if cannot find and hold requested number of seats.
     */
    public String holdAvailableSeats(int numSeats, String customerEmail) {
        SeatHold seatHold = findAndHoldSeats(numSeats, customerEmail);
        String res = null;
        if (seatHold != null) {
            seatHoldMap.put(seatHold.getId(), seatHold);
            res = seatHold.getId();
        }

        logger.debug(res);
        return res;
    }
}
