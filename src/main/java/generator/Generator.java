package generator;

/**
 * Generator generates unique identifications for certain object(s).
 */
public interface Generator {
    /**
     * Generates unique id to identify an object
     * @return unique id
     */
    String generateId();
}