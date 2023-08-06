import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        PrintingTask printingTask = new PrintingTask();
        Thread printingThread = new Thread(printingTask);

        printingThread.start();

        TimeUnit.SECONDS.sleep(5);

        printingTask.setShouldPrint(false);
        System.out.println("Printing task should be stopped");
    }
}
