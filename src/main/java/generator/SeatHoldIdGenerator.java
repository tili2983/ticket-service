package generator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SeatHoldIdGenerator generates unique id
 */
public class SeatHoldIdGenerator implements Generator {

    private static AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * Generates unique id for a seat hold.
     * @return unique id
     */
    public synchronized String generateId() {
        return String.valueOf(idCounter.getAndIncrement());
    }

}
