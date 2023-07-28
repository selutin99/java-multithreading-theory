import model.Message;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public final class MessageBroker {

    private final Queue<Message> messages;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.maxStoredMessages = maxStoredMessages;
        this.messages = new ArrayDeque<>(this.maxStoredMessages);
    }

    public synchronized void produce(Message message, MessageProducingTask messageProducingTask) {
        try {
            while (!shouldProduce(messageProducingTask)) {
                super.wait();
            }
            this.messages.add(message);
            System.out.println(
                    "Message " + message + " is produced by producer "
                    + messageProducingTask.getName() + " Amount of messages before producing: " + (this.messages.size() - 1));
            super.notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(MessageConsumingTask messageConsumingTask) {
        try {
            while (!shouldConsume(messageConsumingTask)) {
                super.wait();
            }

            Message message = this.messages.poll();
            System.out.println(
                    "Message " + message + " is consumed by consumer "
                    + messageConsumingTask.getName() + " Amount of messages before consuming: " + (this.messages.size() + 1));
            super.notify();
            return Optional.ofNullable(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    private boolean shouldProduce(MessageProducingTask messageProducingTask) {
        return this.messages.size() < this.maxStoredMessages
                && this.messages.size() <= messageProducingTask.getMaximalAmountMessageToProduce();
    }

    private boolean shouldConsume(MessageConsumingTask messageConsumingTask) {
        return !this.messages.isEmpty()
                && this.messages.size() >= messageConsumingTask.getMinimalAmountMessageToConsume();
    }
}
