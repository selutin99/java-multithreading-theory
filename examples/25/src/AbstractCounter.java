import java.util.concurrent.locks.Lock;

public abstract class AbstractCounter {

    private long value;

    public long getValue() {
        Lock lock = this.getReadLock();
        lock.lock();
        try {
            return this.value;
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
