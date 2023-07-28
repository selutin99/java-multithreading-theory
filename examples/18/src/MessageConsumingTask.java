import model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsumingTask implements Runnable {

    private final MessageBroker messageBroker;

    private final int minimalAmountMessageToConsume;
    private final String name;

    public MessageConsumingTask(MessageBroker messageBroker,
                                int minimalAmountMessageToConsume,
                                String name) {
        this.messageBroker = messageBroker;
        this.minimalAmountMessageToConsume = minimalAmountMessageToConsume;
        this.name = name;
    }

    public int getMinimalAmountMessageToConsume() {
        return this.minimalAmountMessageToConsume;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(1L);
                Optional<Message> optionalMessage = this.messageBroker.consume(this);
                optionalMessage.orElseThrow(MessageConsumingException::new);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
