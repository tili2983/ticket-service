package service;

import exception.TicketServiceRuntimeException;
import model.Venue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;


public class SimpleTicketServiceTest {
    /**
     * removeExpiredSeatHolds should be called by a parent service in a periodic manner like cron jobs.
     * @throws TicketServiceRuntimeException
     * @throws InterruptedException
     */
    @Test
    public void testRemoveExpiredSeatHoldsBatchProcessing() throws TicketServiceRuntimeException, InterruptedException {
        // ARRANGE
        final Venue v1 = new Venue(15,20);
        SimpleTicketService ser = new SimpleTicketService(v1, 4);
        final String testEmail = "john.doe@xyz.com";

        // ACT
        String sh1 = ser.holdAvailableSeats(14, testEmail);
        String sh2 = ser.holdAvailableSeats(55, testEmail);
        String sh3 = ser.holdAvailableSeats(66, testEmail);
        String sh4 = ser.holdAvailableSeats(7, testEmail);

        ser.removeExpiredSeatHolds();
        String confirmationCode1 = ser.reserveSeats(sh1, testEmail);
        String confirmationCode2 = ser.reserveSeats(sh2, testEmail);

        Thread.sleep(5_000);

        ser.removeExpiredSeatHolds();
        String confirmationCode3 = ser.reserveSeats(sh3, testEmail);
        String confirmationCode4 = ser.reserveSeats(sh4, testEmail);

        // ASSERT
        assertNotNull(confirmationCode1);
        assertNotNull(confirmationCode2);
        assertNull(confirmationCode3);
        assertNull(confirmationCode4);
    }

    @Test(expected = TicketServiceRuntimeException.class)
    public void TicketServiceNull() throws TicketServiceRuntimeException {
        new SimpleTicketService(null, 120_000L);
    }

    @Test
    public void testNumberOfAvailableSeats_all_open() {
        // ARRANGE
        final Venue venue = new Venue(6, 6);
        SimpleTicketService ser = new SimpleTicketService(venue, 12_000);
        final int expected = 6 * 6;

        // ACT
        int actual = ser.numSeatsAvailable();

        // ASSERT
        assertEquals(expected, actual);
    }

    @Test
    public void testNumberOfAvailableSeats_hold_10() {
        // ARRANGE
        final Venue venue = new Venue(6, 6);
        SimpleTicketService ser = new SimpleTicketService(venue, 12_000);
        final int expected = 26;
        ser.holdAvailableSeats(10, "abc@d.com");

        // ACT
        int actual = ser.numSeatsAvailable();

        // ASSERT
        assertEquals(expected, actual);
    }

    @Test
    public void testReserveSeats_normal() {
        // ARRANGE
        final Venue v = new Venue(6,8);
        SimpleTicketService ser = new SimpleTicketService(v, 1_000);
        final String email = "abc_x-m7yz@def764.com";
        // -- HOLD SEATS
        String id1 = ser.holdAvailableSeats(2, email);
        String id2 = ser.holdAvailableSeats(4, email);
        String id3 = ser.holdAvailableSeats(12, email);
        String id4 = ser.holdAvailableSeats(20, email);
        String id5 = ser.holdAvailableSeats(3, email);
        assertEquals(7, ser.numSeatsAvailable());
        assertEquals(41, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());

        // -- RESERVE SEATS
        assertNotNull(ser.reserveSeats(id1, email));
        assertEquals(7, ser.numSeatsAvailable());
        assertEquals(39, ser.numSeatsOnHold());
        assertEquals(2, ser.numSeatsReserved());

        assertNotNull(ser.reserveSeats(id2, email));
        assertNotNull(ser.reserveSeats(id3, email));
        assertNotNull(ser.reserveSeats(id4, email));
        assertNotNull(ser.reserveSeats(id5, email));
        assertEquals(7, ser.numSeatsAvailable());
        assertEquals(0, ser.numSeatsOnHold());
        assertEquals(41, ser.numSeatsReserved());
    }

    @Test
    public void testReserveSeats_invalid_input() {
        // ARRANGE
        final Venue v = new Venue(6,8);
        SimpleTicketService ser = new SimpleTicketService(v, 1_000);
        final String email = "abc_x-m7yz@def764.com";
        // -- HOLD SEATS
        String id1 = ser.holdAvailableSeats(2, email);

        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());

        // invalid id
        assertNull(ser.reserveSeats("7777777777", email));

        // invalid email
        assertNull(ser.reserveSeats(id1, "d=++@#@..--.feiwl"));

        // incorrect email
        assertNull(ser.reserveSeats(id1, "1111111abc_x-m7yz@def764.com"));

        //ASSERT
        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());
    }

    @Test
    public void testReserveSeats_invalid_id() {
        // ARRANGE
        final Venue v = new Venue(6,8);
        SimpleTicketService ser = new SimpleTicketService(v, 1_000);
        final String email = "abc_x-m7yz@def764.com";
        // -- HOLD SEATS
        String id1 = ser.holdAvailableSeats(2, email);

        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());

        // invalid id
        assertNull(ser.reserveSeats("7777777777", email));

        //ASSERT
        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());
    }

    @Test
    public void testReserveSeats_invalid_email() {
        // ARRANGE
        final Venue v = new Venue(6,8);
        SimpleTicketService ser = new SimpleTicketService(v, 1_000);
        final String email = "abc_x-m7yz@def764.com";
        // -- HOLD SEATS
        String id1 = ser.holdAvailableSeats(2, email);

        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());

        // invalid email
        assertNull(ser.reserveSeats(id1, "d=++@#@..--.feiwl"));

        //ASSERT
        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());
    }

    @Test
    public void testReserveSeats_incorrect_email() {
        // ARRANGE
        final Venue v = new Venue(6,8);
        SimpleTicketService ser = new SimpleTicketService(v, 1_000);
        final String email = "abc_x-m7yz@def764.com";
        // -- HOLD SEATS
        String id1 = ser.holdAvailableSeats(2, email);

        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());

        // incorrect email
        assertNull(ser.reserveSeats(id1, "1111111abc_x-m7yz@def764.com"));

        //ASSERT
        assertEquals(46, ser.numSeatsAvailable());
        assertEquals(2, ser.numSeatsOnHold());
        assertEquals(0, ser.numSeatsReserved());
    }

}
