package rater;

import static org.junit.Assert.assertEquals;
import model.Venue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FrontMiddleBetterRaterTest {
    private Rater rater;

    @Test
    public void testRate_8x4() {
        Venue venue = new Venue(8, 4);

        // assert row 1.
        assertEquals(.63d, rater.rateSeat(1, 1, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(2, 1, venue), 0.009);
        assertEquals(.88d, rater.rateSeat(3, 1, venue), 0.009);
        assertEquals(1.0d, rater.rateSeat(4, 1, venue), 0.00);
        assertEquals(1.0d, rater.rateSeat(5, 1, venue), 0.00);
        assertEquals(.88d, rater.rateSeat(6, 1, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(7, 1, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(8, 1, venue), 0.009);

        // assert row 2.
        assertEquals(.50d, rater.rateSeat(1, 2, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(2, 2, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(3, 2, venue), 0.009);
        assertEquals(.88d, rater.rateSeat(4, 2, venue), 0.009);
        assertEquals(.88d, rater.rateSeat(5, 2, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(6, 2, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(7, 2, venue), 0.009);
        assertEquals(.50d, rater.rateSeat(8, 2, venue), 0.009);

        // assert row 3.
        assertEquals(.38d, rater.rateSeat(1, 3, venue), 0.009);
        assertEquals(.50d, rater.rateSeat(2, 3, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(3, 3, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(4, 3, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(5, 3, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(6, 3, venue), 0.009);
        assertEquals(.50d, rater.rateSeat(7, 3, venue), 0.009);
        assertEquals(.38d, rater.rateSeat(8, 3, venue), 0.009);

        // assert row 4.
        assertEquals(.25d, rater.rateSeat(1, 4, venue), 0.009);
        assertEquals(.38d, rater.rateSeat(2, 4, venue), 0.009);
        assertEquals(.50d, rater.rateSeat(3, 4, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(4, 4, venue), 0.009);
        assertEquals(.63d, rater.rateSeat(5, 4, venue), 0.009);
        assertEquals(.50d, rater.rateSeat(6, 4, venue), 0.009);
        assertEquals(.38d, rater.rateSeat(7, 4, venue), 0.009);
        assertEquals(.25d, rater.rateSeat(8, 4, venue), 0.009);
    }

    @Test
    public void testRate_15x1() {
        Venue venue = new Venue(15, 1);

        // assert row 1.
        assertEquals(.563d, rater.rateSeat(1, 1, venue), 0.009);
        assertEquals(.625d, rater.rateSeat(2, 1, venue), 0.009);
        assertEquals(.688d, rater.rateSeat(3, 1, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(4, 1, venue), 0.009);
        assertEquals(.813d, rater.rateSeat(5, 1, venue), 0.009);
        assertEquals(.875d, rater.rateSeat(6, 1, venue), 0.009);
        assertEquals(.938d, rater.rateSeat(7, 1, venue), 0.009);
        assertEquals(1.0d, rater.rateSeat(8, 1, venue), 0.000);
        assertEquals(.938d, rater.rateSeat(9, 1, venue), 0.009);
        assertEquals(.875d, rater.rateSeat(10, 1, venue), 0.009);
        assertEquals(.813d, rater.rateSeat(11, 1, venue), 0.009);
        assertEquals(.75d, rater.rateSeat(12, 1, venue), 0.009);
        assertEquals(.688d, rater.rateSeat(13, 1, venue), 0.009);
        assertEquals(.625d, rater.rateSeat(14, 1, venue), 0.009);
        assertEquals(.563d, rater.rateSeat(15, 1, venue), 0.009);
    }

    @Before
    public void setUp() {
        rater = new FrontMiddleBetterRater();
    }

    @After
    public void tearDown() {
        rater = null;
    }
}

