package generator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ReservationConfirmationIdGenerator generates unique reservation confirmation id
 */
public class ReservationConfirmationIdGenerator implements Generator {

    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * Generates unique id for a confirmation.
     * @return unique id
     */
    public synchronized String generateId() {
        return String.valueOf(counter.getAndIncrement());
    }

}
