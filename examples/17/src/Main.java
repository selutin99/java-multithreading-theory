public class Main {

    public static void main(String[] args) {
        int brokerMaxStorage = 5;
        MessageBroker messageBroker = new MessageBroker(brokerMaxStorage);

        Thread producingThread = new Thread(new MessageProducingTask(messageBroker));
        Thread consumingThread = new Thread(new MessageConsumingTask(messageBroker));

        producingThread.start();
        consumingThread.start();
    }
}
