package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {
    /**
     * 16:39:03.146 [     work] 작업 중
     * 16:39:06.153 [     work] 작업 중
     * 16:39:07.128 [     main] 작업 중단 지시 thread.interrupt()
     * 16:39:07.132 [     main] work 스레드 인터럽트 상태1 = true
     * 16:39:07.133 [     work] work 스레드 인터럽트 상태2 = false
     * 16:39:07.134 [     work] interrupt message = sleep interrupted
     * 16:39:07.134 [     work] state = RUNNABLE
     * 16:39:07.134 [     work] 자원 정리
     * 16:39:07.134 [     work] 작업 종료
     */
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    log("작업 중");
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());
                log("interrupt message = " + e.getMessage());
                log("state = " + Thread.currentThread().getState());
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
