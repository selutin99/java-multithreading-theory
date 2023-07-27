import model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask implements Runnable {

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    public MessageProducingTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        this.messageFactory = new MessageFactory();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message producedMessage = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(1L);
                this.messageBroker.produce(producedMessage);
                System.out.println("Message " + producedMessage + " is produced");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class MessageFactory {

        private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
        private static final String TEMPLATE_MESSAGE_DATA = "Message#%d";

        private int nextMessageIndex;

        public MessageFactory() {
            this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
        }

        public Message create() {
            return new Message(String.format(TEMPLATE_MESSAGE_DATA, this.nextMessageIndex++));
        }
    }
}
