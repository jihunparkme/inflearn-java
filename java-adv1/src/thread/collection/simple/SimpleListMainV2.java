package thread.collection.simple;

import thread.collection.simple.list.BasicList;
import thread.collection.simple.list.SimpleList;

import static util.MyLogger.log;

public class SimpleListMainV2 {

    /**
     * [     main] BasicList
     * [ Thread-1] Thread-1: list.add(A)
     * [ Thread-2] Thread-2: list.add(B)
     * [     main] [B, null], size = 2, capacity = 5
     */
    public static void main(String[] args) throws InterruptedException {
        test(new BasicList());
    }

    private static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        // A를 리스트에 저장
        Runnable addA = () -> {
            list.add("A");
            log("Thread-1: list.add(A)");
        };
        // B를 리스트에 저장
        Runnable addB = () -> {
            list.add("B");
            log("Thread-2: list.add(B)");
        };

        Thread thread1 = new Thread(addA, "Thread-1");
        Thread thread2 = new Thread(addB, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        log(list);
    }
}
