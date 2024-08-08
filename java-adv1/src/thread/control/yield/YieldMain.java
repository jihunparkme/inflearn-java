package thread.control.yield;

public class YieldMain {
    static final int THREAD_COUNT = 1000;

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new MyRunnable());
            thread.start();
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);
                /** 1. empty
                 * 운영체제의 스레드 스케줄링을 따름
                 * 대략 0.01초(10ms)정도 하나의 스레드가 실행되고, 다른 스레드로 넘어감
                 */

                /** 2. sleep
                 * TIMED_WAITING 상태로 전환되면 스레드는 CPU 자원을 사용하지 않고, 실행 스케줄링에서 잠시 제외(다른 스레드에 실행을 양보)
                 * 상태가 전환되는 복잡한 과정(context switch)과 특정 시간만큼 스레드가 실행되지 않는 단점이 존재
                 */
                // sleep(1);

                /**
                 * 3. yield
                 * 현재 실행 중인 스레드가 자발적으로 CPU를 양보(RUNNABLE 상태를 유지하면서 CPU를 양보)하여 다른 스레드가 실행될 수 있도록 함
                 * 양보할 사람이 없다면 본인 스레드가 계속 실행
                 */
                // Thread.yield();
            }
        }
    }
}
