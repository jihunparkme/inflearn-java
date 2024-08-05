package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {
    /**
     * [     work] ...
     * [     work] 작업 중
     * [     main] 작업 중단 지시 - thread.interrupt()
     * [     work] 작업 중 => (while 문 중간에 인터럽트 요청)
     * [     main] work 스레드 인터럽트 상태1 = true
     * [     work] work 스레드 인터럽트 상태2 = false => (Thread.interrupted() 는 인터럽트 상태를 false 로 변경)
     * [     work] 자원 정리 시도
     * [     work] 자원 정리 완료
     * [     work] 작업 종료
     */
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100);
        log("작업 중단 지시 - thread.interrupt()");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted()); // true
    }

    static class MyTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) { // 인터럽트 상태 변경
                log("작업 중");
            }
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // false

            try {
                log("자원 정리 시도");
                Thread.sleep(1000);
                log("자원 정리 완료");
            } catch (InterruptedException e) { // 인터럽트 상태가 false 이므로 예외를 타지 않음
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }
}