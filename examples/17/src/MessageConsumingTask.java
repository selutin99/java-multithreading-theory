import model.Message;

import java.util.concurrent.TimeUnit;

public class MessageConsumingTask implements Runnable {

    private final MessageBroker messageBroker;

    public MessageConsumingTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(1L);
                Message consumedMessage = this.messageBroker.consume();
                System.out.println("Message " + consumedMessage + " is consumed");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
