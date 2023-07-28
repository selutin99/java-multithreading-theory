import model.Message;

public final class MessageFactory {

    private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
    private static final String TEMPLATE_MESSAGE_DATA = "Message#%d";

    private int nextMessageIndex;

    public MessageFactory() {
        this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
    }

    public Message create() {
        return new Message(String.format(TEMPLATE_MESSAGE_DATA, increment()));
    }

    private synchronized int increment() {
        return this.nextMessageIndex++;
    }
}
