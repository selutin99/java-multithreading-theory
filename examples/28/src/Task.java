import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Task implements Runnable {

    private static final String TRY_ACQUIRE_LOCK = "Thread '%s' is trying to acquire lock '%s'\n";
    private static final String SUCCESS_ACQUIRE_LOCK = "Thread '%s' acquired lock '%s'\n";
    private static final String RELEASE_LOCK = "Thread '%s' released lock '%s'\n";

    private static final String NAME_FIRST_LOCK = "firstLock";
    private static final String NAME_SECOND_LOCK = "secondLock";

    private final Lock firstLock;
    private final Lock secondLock;

    public Task(Lock firstLock, Lock secondLock) {
        this.firstLock = firstLock;
        this.secondLock = secondLock;
    }

    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.printf(TRY_ACQUIRE_LOCK, currentThreadName, NAME_FIRST_LOCK);

        this.firstLock.lock();
        try {
            System.out.printf(SUCCESS_ACQUIRE_LOCK, currentThreadName, NAME_FIRST_LOCK);
            TimeUnit.MILLISECONDS.sleep(50);
            while (!this.tryAcquireSecondLock()) {
                TimeUnit.MILLISECONDS.sleep(50);
                this.firstLock.unlock();
                System.out.printf(RELEASE_LOCK, currentThreadName, NAME_FIRST_LOCK);
                TimeUnit.MILLISECONDS.sleep(50);
                System.out.printf(TRY_ACQUIRE_LOCK, currentThreadName, NAME_FIRST_LOCK);
                this.firstLock.lock();
                System.out.printf(SUCCESS_ACQUIRE_LOCK, currentThreadName, NAME_FIRST_LOCK);
                TimeUnit.MILLISECONDS.sleep(50);
            }
            try {
                System.out.printf(SUCCESS_ACQUIRE_LOCK, currentThreadName, NAME_SECOND_LOCK);
            } finally {
                this.secondLock.unlock();
                System.out.printf(RELEASE_LOCK, currentThreadName, NAME_SECOND_LOCK);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.firstLock.unlock();
            System.out.printf(RELEASE_LOCK, currentThreadName, NAME_FIRST_LOCK);
        }
    }

    private boolean tryAcquireSecondLock() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.printf(TRY_ACQUIRE_LOCK, currentThreadName, NAME_SECOND_LOCK);
        return this.secondLock.tryLock();
    }
}
