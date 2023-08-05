import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public abstract class AbstractCounter {

    private long value;

    public Optional<Long> getValue() {
        Lock lock = this.getReadLock();
        lock.lock();
        try {
            TimeUnit.SECONDS.sleep(1);
            return Optional.of(this.value);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public void increment() {
        Lock lock = this.getWriteLock();
        lock.lock();
        try {
            this.value++;
        } finally {
            lock.unlock();
        }
    }

    protected abstract Lock getReadLock();

    protected abstract Lock getWriteLock();
}
