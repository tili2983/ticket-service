package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SimpleSeatHoldTest {

    @Test
    public void sanityTest() throws Exception {
        final String testEmail = "xyx@wem.com";
        final String seatHoldId = "10";
        List<Seat> s = new LinkedList<>(
                Arrays.asList(new Seat(1, 2, 0.80),
                              new Seat(2, 3, 0.54),
                              new Seat(3, 8, 0.4)));
        SeatHold s1 = new SimpleSeatHold(seatHoldId, testEmail, s);
        Timestamp t = Timestamp.from(Instant.now());
        Assert.assertTrue(s1.getCreationTimestamp().getTime()- t.getTime() <= 3);
        Assert.assertEquals(testEmail, s1.getEmail());
        Assert.assertEquals(seatHoldId, s1.getId());
        Assert.assertTrue(s.containsAll(s1.getSeats()));
    }

}
