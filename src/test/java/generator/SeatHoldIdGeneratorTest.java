package generator;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SeatHoldIdGeneratorTest {
    Logger logger = Logger.getLogger(SeatHoldIdGeneratorTest.class);
    Set<String> s;
    boolean foundDuplicate;
    SeatHoldIdGenerator sut;

    @Before
    public void setUp() {
        s = new HashSet<>();
        sut = new SeatHoldIdGenerator();
        foundDuplicate = false;
    }

    @Test
    public void GenerateUniqueIdsMultiThread() {
        for (int i = 0; i < 1000; i++) {
            GenerateIdThread r = new GenerateIdThread();
            Thread t = new Thread(r);
            t.start();
        }
        Assert.assertFalse(foundDuplicate);
    }


    class GenerateIdThread implements Runnable {
        private String id;

        @Override
        public void run() {
            try {
                id = sut.generateId();
                synchronized (s) {
                    if (s.contains(id)) foundDuplicate = true;
                    s.add(id);
                }

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
