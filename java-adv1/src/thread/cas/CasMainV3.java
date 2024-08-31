package thread.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CasMainV3 {
    private static final int THREAD_COUNT = 2;

    /**
     * start value = 0
     *
     * 12:21:42.820 [ Thread-1] getValue: 0
     * 12:21:42.820 [ Thread-0] getValue: 0
     * 12:21:42.822 [ Thread-0] result: false
     * 12:21:42.822 [ Thread-1] result: true
     * 12:21:42.925 [ Thread-0] getValue: 1
     * 12:21:42.925 [ Thread-0] result: true
     *
     * AtomicInteger resultValue:2
     */
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        Runnable runnable = () -> incrementAndGet(atomicInteger);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int result = atomicInteger.get();
        System.out.println(atomicInteger.getClass().getSimpleName() + " resultValue:" + result);
    }

    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;
        boolean result;
        do {
            getValue = atomicInteger.get();
            sleep(100); // 스레드 동시 실행을 위한 대기
            log("getValue: " + getValue);
            result = atomicInteger.compareAndSet(getValue, getValue + 1);
            log("result: " + result);
        } while (!result);
        return getValue + 1; // 다른 스레드가 증가시킨 값이 나올 수도 있기 때문에 get() 대신 +1
    }
}
