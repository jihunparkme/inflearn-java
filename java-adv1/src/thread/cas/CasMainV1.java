package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV1 {
    /**
     * compareAndSet(0, 1)
     * - atomicInteger 가 가지고 있는 값이 현재 0이면 이 값을 1로 변경
     * - 원자적으로 실행 -> CAS(compareAndSet) 연산
     */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        boolean result1 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result1 = " + result1 + ", value = " + atomicInteger.get());

        boolean result2 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result2 = " + result2 + ", value = " + atomicInteger.get());
    }
}