public class Main {

    public static void main(String[] args) {
        int brokerMaxStorage = 15;
        MessageBroker messageBroker = new MessageBroker(brokerMaxStorage);

        MessageFactory messageFactory = new MessageFactory();

        Thread firstProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, brokerMaxStorage, "PRODUCER 1"));
        Thread secondProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, 10, "PRODUCER 2"));
        Thread thirdProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, 5, "PRODUCER 3"));

        Thread firstConsumingThread = new Thread(new MessageConsumingTask(messageBroker, 0, "CONSUMER 1"));
        Thread secondConsumingThread = new Thread(new MessageConsumingTask(messageBroker, 6, "CONSUMER 2"));
        Thread thirdConsumingThread = new Thread(new MessageConsumingTask(messageBroker, 11, "CONSUMER 3"));

        firstProducingThread.start();
        secondProducingThread.start();
        thirdProducingThread.start();

        firstConsumingThread.start();
        secondConsumingThread.start();
        thirdConsumingThread.start();
    }
}
