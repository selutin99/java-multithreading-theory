# 21. Lock, ReentrantLock

Имеются 2 вида защиты кода от параллельного доступа.

1 метод это:
```java
synchonized(object) {
    code
}
```

Того же самого можно добиться используя ReentrantLock. Однако у класса ReentrantLock есть дополнительный функционал.
Защита блока кода с помощью ReentrantLock выглядит следующим образом:
```java
final Lock lock = new ReentantLock();
...
lock.lock();
try {
    code
    return;
} finally {
    lock.unlock();
}
```

> Важно помещать разблокировку (`unlock()`) в блок `finally`. Чтобы блокировка точно снималась. А 
> оператор `return` должен вызываться в блоке `try`. 

Напишем генератор четных чисел.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Runner {
    public static void main(String[] args) {
        EvenNumberGenerator evenNumberGenerator = new EvenNumberGenerator();
        Runnable generatingTask = () -> IntStream.range(0, 100).forEach(i -> System.out.println(evenNumberGenerator.generate()));
        
        Thread firstThread = new Thread(generatingTask);
        firstThread.start();

        Thread secondThread = new Thread(generatingTask);
        secondThread.start();

        Thread thirdThread = new Thread(generatingTask);
        thirdThread.start();
    }

    private static final class EvenNumberGenerator {
        
        private final Lock lock;

        private int previousGenetated;

        public EvenNumberGenerator() {
            this.previousGenetated = -2;
            this.lock = new ReentrantLock();
        }

        public int generate() {
            this.lock.lock();
            try {
                return this.previousGenetated += 2;
            } finally {
                this.lock.unlock();
            }
        }
    }
}
```