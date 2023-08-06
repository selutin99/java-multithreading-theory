import java.util.concurrent.TimeUnit;

public class PrintingTask implements Runnable {

    private volatile boolean shouldPrint = true;

    @Override
    public void run() {
        try {
            while (this.shouldPrint) {
                System.out.println("I am working");
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setShouldPrint(boolean shouldPrint) {
        this.shouldPrint = shouldPrint;
    }
}
