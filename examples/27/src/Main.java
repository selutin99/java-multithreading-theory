import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        Lock firstLock = new ReentrantLock();
        Lock secondLock = new ReentrantLock();

        Thread firstGivenThread = new Thread(new Task(firstLock, secondLock));
        Thread secondGivenThread = new Thread(new Task(secondLock, firstLock));

        firstGivenThread.start();
        secondGivenThread.start();
    }
}
