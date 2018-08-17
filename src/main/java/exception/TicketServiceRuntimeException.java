package exception;

public class TicketServiceRuntimeException extends RuntimeException {
    public TicketServiceRuntimeException() {
        super();
    }
    public TicketServiceRuntimeException(String message) {
        super(message);
    }
    public TicketServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public TicketServiceRuntimeException(Throwable cause) {
        super(cause);
    }
}
