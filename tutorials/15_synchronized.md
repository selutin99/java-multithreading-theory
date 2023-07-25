# 15. Ключевое слово `synchronized`

Проблема состояния гонки из предыдущего урока может решаться с помощью ключевого слова `synchronized`.

В прошлом уроке написана программа, суммирующая цифры от 1 до указанного в 2 потоках. Код работает иногда
правильно, а иногда нет. Вся проблема заключалась не в атомарности операции инкремента.

```java
public class Runner {

    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread firstThread = createIncrementingCounterThread(500);
        Thread secondThread = createIncrementingCounterThread(600);
        
        firstThread.start();
        secondThread.start();
    
        firstThread.join();
        secondThread.join();

        System.out.println(counter);
    }

    private static Thread createIncrementingCounterThread(int incrementAmount) {
        return new Thread(() -> java.util.stream.IntStream.range(0, incrementAmount).forEach(i -> counter++));
    }
}
```

Если вынести инкремент в отдельный метод с ключевым словом `synchronized`, то такой метод сможет выполняться 1
потоком одновременно. Никакой другой поток не сможет начать выполнять код в этом методе, пока другой поток не завершит
работу в этом методе. Например:

```java
public static synchronized void incrementCounter() {
    counter++;
}
```

Таким образом решается проблема состояния гонки.