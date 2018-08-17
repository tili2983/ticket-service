package service;

import model.Seat;
import model.Venue;
import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

public class SimpleSeatingServiceTest {
    static Logger logger = Logger.getLogger(SimpleSeatingServiceTest.class);

    @Test
    public void testHoldSeats_4x4() throws Exception {
        // ARRANGE
        final Venue v = new Venue(4, 4);
        SimpleSeatingService service = new SimpleSeatingService(v);
        List<Seat> seats = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            seats.add(service.getNextBestSeat());
        }

        // ACT
        service.holdSeats(seats);

        // ASSERT
        // Check same seats are marked hold.
        for (int i = 0; i < 5; i++) {
            assertEquals(SimpleSeatingService.HOLD, seats.get(i).getSeatState());
        }

        // ASSERT
        // Check counters
        assertEquals(11, service.getNumOpenSeats());
        assertEquals(5, service.getNumHoldSeats());
        assertEquals(0, service.getNumReservedSeats());
    }

    @Test
    public void testReserveSeats_4x4() throws Exception {
        // ARRANGE
        final Venue v = new Venue(4, 4);
        SimpleSeatingService service = new SimpleSeatingService(v);
        List<Seat> seats = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            seats.add(service.getNextBestSeat());
        }
        service.holdSeats(seats);

        // ACT
        service.reserveSeats(seats);

        // ASSERT
        // Check same seats are marked hold.
        for (int i = 0; i < 5; i++) {
            assertEquals(SimpleSeatingService.RESERVED, seats.get(i).getSeatState());
        }

        // ASSERT
        // Check counters
        assertEquals(11, service.getNumOpenSeats());
        assertEquals(0, service.getNumHoldSeats());
        assertEquals(5, service.getNumReservedSeats());
    }

    @Test
    public void testOpenSeats_4x4() throws Exception {
        // ARRANGE
        final Venue v = new Venue(4, 4);
        SimpleSeatingService service = new SimpleSeatingService(v);
        List<Seat> seats = new LinkedList<>();

        for (int i = 0; i < 16; i++) {
            seats.add(service.getNextBestSeat());
        }
        // hold seats
        service.holdSeats(seats);

        // open seats
        // ACT
        service.openSeats(seats);

        // ASSERT
        // Check same seats are marked hold.
        for (int i = 0; i < 16; i++) {
            assertEquals(SimpleSeatingService.OPEN, seats.get(i).getSeatState());
        }

        // ASSERT
        // Check counters
        assertEquals(16, service.getNumOpenSeats());
        assertEquals(0, service.getNumHoldSeats());
        assertEquals(0, service.getNumReservedSeats());
    }

    @Test
    public void sanity_venue_4x4() {
        // ARRANGE
        final Venue v = new Venue(4, 4);

        // ACT
        SimpleSeatingService service = new SimpleSeatingService(v);

        // ASSERT
        assertEquals(16, service.getNumOpenSeats());
        assertEquals(0, service.getNumHoldSeats());
        assertEquals(0, service.getNumReservedSeats());
    }

    @Test
    public void sanity_venue_15x40() {
        // ARRANGE
        final Venue v = new Venue(15, 40);

        // ACT
        SimpleSeatingService service = new SimpleSeatingService(v);

        // ASSERT
        assertEquals(600, service.getNumOpenSeats());
        assertEquals(0, service.getNumHoldSeats());
        assertEquals(0, service.getNumReservedSeats());
    }

    @Test
    public void testGetNextBestSeat_all_open() {
        // ARRANGE
        final Venue v = new Venue(4, 4);
        final String expectedSeatCode = "1-2";

        // ACT
        SimpleSeatingService service = new SimpleSeatingService(v);

        Seat seat = service.getNextBestSeat();

        // ASSERT
        assertEquals(expectedSeatCode, seat.getSeatCode());
    }

    @Test
    public void testGetNextBestSeat_one_seat_on_temp_hold() {
        // ARRANGE
        final Venue v = new Venue(4, 4);
        final String expectedSeatCode = "1-3";

        // ACT
        SimpleSeatingService service = new SimpleSeatingService(v);
        // one seat on temp_hold
        service.getNextBestSeat();

        Seat s2 = service.getNextBestSeat();

        // ASSERT
        assertEquals(expectedSeatCode, s2.getSeatCode());
    }

    @Test
    public void testGetNextBestSeat_out_of_seats() {
        // ARRANGE
        final Venue v = new Venue(4, 4);

        // ACT
        SimpleSeatingService service = new SimpleSeatingService(v);

        // all seats on temp_hold
        for (int i = 0; i < 16; i++) {
            service.getNextBestSeat();
        }

        Seat nullSeat = service.getNextBestSeat();

        // ASSERT
        assertNull(nullSeat);
    }
}
