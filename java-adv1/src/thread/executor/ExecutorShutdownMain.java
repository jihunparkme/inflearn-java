package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

public class ExecutorShutdownMain {

    /**
     * [pool-1-thread-2] taskB 시작
     * [pool-1-thread-1] taskA 시작
     * [     main] [pool=2, active=2, queuedTasks=2, completedTasks=0]
     * [     main] == shutdown 시작 ==
     * [     main] 서비스 정상 종료 시도
     * [pool-1-thread-2] taskB 완료
     * [pool-1-thread-2] taskC 시작
     * [pool-1-thread-1] taskA 완료
     * [pool-1-thread-1] longTask 시작
     * [pool-1-thread-2] taskC 완료
     * [     main] 서비스 정상 종료 실패 -> 강제 종료 시도
     * [pool-1-thread-1] 인터럽트 발생, sleep interrupted
     * [     main] == shutdown 완료 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=4]
     *
     * Exception in thread "pool-1-thread-1" java.lang.RuntimeException: java.lang.InterruptedException: sleep interrupted
     * 	at util.ThreadUtils.sleep(ThreadUtils.java:12)
     * 	at thread.executor.RunnableTask.run(RunnableTask.java:23)
     * 	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
     * 	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
     * 	at java.base/java.lang.Thread.run(Thread.java:1583)
     * Caused by: java.lang.InterruptedException: sleep interrupted
     * 	at java.base/java.lang.Thread.sleep0(Native Method)
     * 	at java.base/java.lang.Thread.sleep(Thread.java:509)
     * 	at util.ThreadUtils.sleep(ThreadUtils.java:9)
     * 	...
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(2);
        es.execute(new RunnableTask("taskA"));
        es.execute(new RunnableTask("taskB"));
        es.execute(new RunnableTask("taskC"));
        es.execute(new RunnableTask("longTask", 100_000));
        printState(es);

        log("== shutdown 시작 ==");
        shutdownAndAwaitTermination(es);

        log("== shutdown 완료 ==");
        printState(es);
    }

    static void shutdownAndAwaitTermination(ExecutorService es) {
        /**
         * shutdown()
         * - 새로운 작업을 받지 않음
         * - 처리 중이거나 큐에 이미 대기중인 작업은 처리.
         * - 이후에 풀의 스레드 종료
         * (non-blocking,)
         */
        es.shutdown();
        try {
            // 이미 대기중인 작업들을 모두 완료할 때 까지 10초 대기
            log("서비스 정상 종료 시도");
            if (!es.awaitTermination(10, TimeUnit.SECONDS)) {
                // 정상 종료가 너무 오래 걸리면...
                log("서비스 정상 종료 실패 -> 강제 종료 시도");
                es.shutdownNow();
                // 작업이 취소될 때 까지 대기
                if (!es.awaitTermination(10, TimeUnit.SECONDS)) {
                    log("서비스가 종료되지 않았습니다.");
                }
            }
        } catch (InterruptedException ex) {
            // awaitTermination() 으로 대기중인 현재 스레드가 인터럽트 될 수 있음
            es.shutdownNow();
        }
    }
}
