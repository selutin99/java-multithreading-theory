public class MessageConsumingException extends RuntimeException {

    public MessageConsumingException() {
    }

    public MessageConsumingException(String message) {
        super(message);
    }

    public MessageConsumingException(Exception cause) {
        super(cause);
    }

    public MessageConsumingException(String message, Exception cause) {
        super(message, cause);
    }
}
