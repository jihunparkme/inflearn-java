package thread.cas.increment;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 멀티스레드 상황에서 안전하게 증가 연산을 수행할 수 있는 AtomicInteger 클래스 제공
 */
public class MyAtomicInteger implements IncrementInteger {

    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void increment() {
        atomicInteger.incrementAndGet();
    }

    @Override
    public int get() {
        return atomicInteger.get();
    }
}
