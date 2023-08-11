import java.util.concurrent.atomic.AtomicInteger;

public class EvenNumberGenerator {

    private static final int DELTA = 2;

    private final AtomicInteger value = new AtomicInteger();

    public int generate() {
        return this.value.getAndAdd(DELTA);
    }

    public int getValue() {
        return this.value.intValue();
    }
}
