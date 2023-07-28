import model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask implements Runnable {

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    private final int maximalAmountMessageToProduce;
    private final String name;

    public MessageProducingTask(MessageBroker messageBroker,
                                MessageFactory messageFactory,
                                int maximalAmountMessageToProduce,
                                String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.maximalAmountMessageToProduce = maximalAmountMessageToProduce;
        this.name = name;
    }

    public int getMaximalAmountMessageToProduce() {
        return this.maximalAmountMessageToProduce;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message producedMessage = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(1L);
                this.messageBroker.produce(producedMessage, this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
