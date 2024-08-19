package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV2 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ParkTask(), "Thread-1");
        thread1.start();

        sleep(100);
        log("Thread-1 state: " + thread1.getState());
    }

    /**
     * [ Thread-1] park 시작, 2초 대기
     * [     main] Thread-1 state: TIMED_WAITING
     * [ Thread-1] park 종료, state: RUNNABLE
     * [ Thread-1] 인터럽트 상태: false
     */
    static class ParkTask implements Runnable {

        @Override
        public void run() {
            log("park 시작, 2초 대기");
            LockSupport.parkNanos(2000_000000); // 2초 뒤어 깨어남
            log("park 종료, state: " + Thread.currentThread().getState());
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        }
    }
}
