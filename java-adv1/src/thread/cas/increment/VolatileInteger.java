package thread.cas.increment;

/**
 * volatile
 * - 여러 CPU 사이에 발생하는 캐시 메모리와 메인 메모리가 동기화 되지 않는 문제를 해결
 *   - CPU 캐시 메모리를 무시하고, 메인 메모리를 직접 사용
 * - 하지만 원자성 문제는 연산 자체가 나누어져 있기 때문에 발생
 *   - volatile 은 연산 차제를 원자적으로 묶어주는 기능이 아님
 */
public class VolatileInteger implements IncrementInteger {

    private volatile int value;

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
