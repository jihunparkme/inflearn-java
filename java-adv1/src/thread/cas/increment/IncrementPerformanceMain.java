package thread.cas.increment;

public class IncrementPerformanceMain {

    public static final long COUNT = 100_000_000;

    /**
     * BasicInteger: ms = 49
     * VolatileInteger: ms = 736
     * SyncInteger: ms = 1141
     * MyAtomicInteger: ms = 831
     *
     * BasicInteger
     * - 가장 빠른 성능
     * - CPU 캐시를 적극 사용
     * - 안전한 임계 영역도 없고, volatile 도 사용하지 않기 때문에 멀티스레드 상황에는 사용 불가
     * - 단일 스레드가 사용하는 경우에 효율적
     *
     * VolatileInteger
     * - volatile 을 사용해서 CPU 캐시를 사용하지 않고 메인 메모리를 사용
     * - 안전한 임계 영역이 없기 때문에 멀티스레드 상황에는 사용 불가
     * - 단일 스레드가 사용하기에는 BasicInteger 보다 느리고, 멀티스레드 상황에도 안전하지 않음
     *
     * SyncInteger
     * - synchronized 를 사용한 안전한 임계 영역이 있기 때문에 멀티스레드 상황에도 안전하게 사용 가능
     * - MyAtomicInteger 보다 성능이 느림
     *
     * MyAtomicInteger
     * - 자바가 제공하는 AtomicInteger 를 사용
     * - 멀티스레드 상황에 안전하게 사용 가능
     * - 성능도 synchronized, Lock(ReentrantLock) 을 사용하는 경우보다 1.5 ~ 2배 정도 빠름
     * - incrementAndGet() 메서드는 락을 사용하지 않고, 원자적 연산을 만들어 냄
     */
    public static void main(String[] args) {
        test(new BasicInteger());
        test(new VolatileInteger());
        test(new SyncInteger());
        test(new MyAtomicInteger());
    }

    private static void test(IncrementInteger incrementInteger) {
        long startMs = System.currentTimeMillis();

        for (long i = 0; i < COUNT; i++) {
            incrementInteger.increment();
        }

        long endMs = System.currentTimeMillis();
        System.out.println(incrementInteger.getClass().getSimpleName() + ": ms = " + (endMs - startMs));
    }
}
