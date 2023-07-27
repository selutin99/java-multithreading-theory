import model.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public final class MessageBroker {

    private final Queue<Message> messages;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.maxStoredMessages = maxStoredMessages;
        this.messages = new ArrayDeque<>(this.maxStoredMessages);
    }

    public synchronized void produce(Message message) {
        try {
            while (this.messages.size() >= this.maxStoredMessages) {
                super.wait();
            }
            this.messages.add(message);
            super.notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Message consume() {
        try {
            while (this.messages.isEmpty()) {
                super.wait();
            }

            Message message = this.messages.poll();
            super.notify();
            return message;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
