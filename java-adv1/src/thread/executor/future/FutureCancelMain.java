package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class FutureCancelMain {

    private static boolean mayInterruptIfRunning = false;

    /**
     * mayInterruptIfRunning == true
     * => Future 를 취소 상태로 변경. 이때 작업이 실행중이라면 Thread.interrupt() 를 호출해서 작업을 중단
     * [     main] Future.state: RUNNING
     * [pool-1-thread-1] 작업 중: 0
     * [pool-1-thread-1] 작업 중: 1
     * [pool-1-thread-1] 작업 중: 2
     * [     main] future.cancel(true) 호출
     * [pool-1-thread-1] 인터럽트 발생
     * [     main] Future.state: CANCELLED
     * [     main] cancel(true) result: true
     * [     main] Future는 이미 취소 되었습니다.
     *
     * mayInterruptIfRunning == false
     * => Future 를 취소 상태로 변경. 단 이미 실행 중인 작업을 중단하지는 않음
     * [     main] Future.state: RUNNING
     * [pool-1-thread-1] 작업 중: 0
     * [pool-1-thread-1] 작업 중: 1
     * [pool-1-thread-1] 작업 중: 2
     * [     main] future.cancel(false) 호출
     * [     main] Future.state: CANCELLED
     * [     main] cancel(false) result: true
     * [     main] Future는 이미 취소 되었습니다.
     * [pool-1-thread-1] 작업 중: 3
     * [pool-1-thread-1] 작업 중: 4
     * [pool-1-thread-1] 작업 중: 5
     * [pool-1-thread-1] 작업 중: 6
     * [pool-1-thread-1] 작업 중: 7
     * [pool-1-thread-1] 작업 중: 8
     * [pool-1-thread-1] 작업 중: 9
     */
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<String> future = es.submit(new MyTask());
        log("Future.state: " + future.state());

        // 일정 시간 후 취소 시도
        sleep(3000);

        // cancel() 호출
        log("future.cancel(" + mayInterruptIfRunning + ") 호출");
        boolean cancelResult1 = future.cancel(mayInterruptIfRunning);
        log("Future.state: " + future.state());
        log("cancel(" + mayInterruptIfRunning + ") result: " + cancelResult1);

        // 결과 확인
        try {
            log("Future result: " + future.get());
        } catch (CancellationException e) { // 런타임 예외
            log("Future는 이미 취소 되었습니다.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Executor 종료
        es.close();
    }

    static class MyTask implements Callable<String> {
        @Override
        public String call() {
            try {
                for (int i = 0; i < 10; i++) {
                    log("작업 중: " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                log("인터럽트 발생");
                return "Interrupted";
            }
            return "Completed";
        }
    }
}
