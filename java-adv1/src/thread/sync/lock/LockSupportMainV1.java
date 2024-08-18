package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV1 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ParkTask(), "Thread-1");
        thread1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
        sleep(100);
        log("Thread-1 state: " + thread1.getState());

        /**
         * 1. unpark 사용
         *
         * [ Thread-1] park 시작
         * [     main] Thread-1 state: WAITING
         * [     main] main -> unpark(Thread-1)
         * [ Thread-1] park 종료, state: RUNNABLE
         * [ Thread-1] 인터럽트 상태: false
         */
        log("main -> unpark(Thread-1)");
        LockSupport.unpark(thread1);

        /**
         * 2. interrupt() 사용
         *
         * [ Thread-1] park 시작
         * [     main] Thread-1 state: WAITING
         * [     main] main -> thread1.interrupt()
         * [ Thread-1] park 종료, state: RUNNABLE
         * [ Thread-1] 인터럽트 상태: true
         */
        //log("main -> thread1.interrupt()");
        //thread1.interrupt();
    }

    static class ParkTask implements Runnable {

        @Override
        public void run() {
            log("park 시작");
            LockSupport.park();
            log("park 종료, state: " + Thread.currentThread().getState());
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        }
    }
}
