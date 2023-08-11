import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        EvenNumberGenerator generator = new EvenNumberGenerator();

        int taskGenerationCounts = 10000;
        Runnable generatingTask = () -> IntStream.range(0, taskGenerationCounts).forEach(i -> generator.generate());

        int amountOfThreads = 5;
        Thread[] generatingThreads = createThreads(generatingTask, amountOfThreads);

        startThreads(generatingThreads);
        waitUnitFinish(generatingThreads);

        int expectedGeneratorValue = amountOfThreads * taskGenerationCounts * 2;
        int actualValue = generator.getValue();
        System.out.println("Expected: " + expectedGeneratorValue + " actual: " + actualValue);
    }

    public static void startThreads(Thread[] threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    public static void waitUnitFinish(Thread[] threads) {
        Arrays.stream(threads).forEach(Main::waitUnitFinish);
    }

    public static void waitUnitFinish(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static Thread[] createThreads(Runnable task, int amountOfThreads) {
        return IntStream.range(0, amountOfThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }
}
