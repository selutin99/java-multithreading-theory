import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // testCounter(Counter::new); // Total amount of reads: 192 649 736; 55
        testCounter(CounterReadWrite::new); // Total amount of reads: 19 987 817; 250
    }

    private static void testCounter(final Supplier<? extends AbstractCounter> counterFactory) throws InterruptedException {
        AbstractCounter counter = counterFactory.get();
        int amountOfThreadsReaders = 50;

        ReadingTask[] readingTasks = createReadingTask(counter, amountOfThreadsReaders);
        Thread[] readingThreads = mapToThreads(readingTasks);

        Runnable incrementingTask = createIncrementingCounterTask(counter);
        int amountOfThreadsWriters = 2;
        Thread[] incrementingThreads = createThreads(incrementingTask, amountOfThreadsReaders);

        startThreads(readingThreads);
        startThreads(incrementingThreads);

        TimeUnit.SECONDS.sleep(5);

        interruptThreads(readingThreads);
        interruptThreads(incrementingThreads);

        waitUnitFinish(readingThreads);

        long totalReads = findTotalAmountOfReads(readingTasks);
        System.out.println("Total amount of reads: " + totalReads);
    }

    private static ReadingTask[] createReadingTask(AbstractCounter counter, int amountOfTasks) {
        return IntStream.range(0, amountOfTasks)
                .mapToObj(i -> new ReadingTask(counter))
                .toArray(ReadingTask[]::new);
    }

    private static Thread[] mapToThreads(Runnable[] tasks) {
        return Arrays.stream(tasks)
                .map(Thread::new)
                .toArray(Thread[]::new);
    }

    private static Runnable createIncrementingCounterTask(AbstractCounter counter) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                incrementCounter(counter);
            }
        };
    }

    private static void incrementCounter(AbstractCounter counter) {
        try {
            counter.increment();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Thread[] createThreads(Runnable task, int amountOfThreads) {
        return IntStream.range(0, amountOfThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }

    private static void startThreads(Thread[] threads) {
        forEach(threads, Thread::start);
    }

    private static void interruptThreads(Thread[] threads) {
        forEach(threads, Thread::interrupt);
    }

    private static void forEach(Thread[] threads, Consumer<Thread> action) {
        Arrays.stream(threads)
                .forEach(action);
    }

    private static void waitUnitFinish(Thread[] threads) {
        forEach(threads, Main::waitUnitFinish);
    }

    private static void waitUnitFinish(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static long findTotalAmountOfReads(ReadingTask[] tasks) {
        return Arrays.stream(tasks)
                .mapToLong(ReadingTask::getAmountOfReads)
                .sum();
    }

    private static final class ReadingTask implements Runnable {

        private final AbstractCounter counter;
        private long amountOfReads;

        public ReadingTask(AbstractCounter counter) {
            this.counter = counter;
        }

        public long getAmountOfReads() {
            return amountOfReads;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                this.counter.getValue();
                this.amountOfReads++;
            }
        }
    }
}
