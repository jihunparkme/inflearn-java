package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class PoolSizeMainV1 {

    /**
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=0]
     * [pool-1-thread-1] task1 시작
     * [     main] task1 -> [pool=1, active=1, queuedTasks = 0, completedTasks = 0]
     * [pool-1-thread-2] task2 시작
     * [     main] task2 -> [pool=2, active=2, queuedTasks = 0, completedTasks = 0]
     * [pool-1-thread-1] task3 시작
     * [     main] task3 -> [pool=2, active=2, queuedTasks = 1, completedTasks = 0]
     * [pool-1-thread-2] task4 시작
     * [     main] task4 -> [pool=2, active=2, queuedTasks = 2, completedTasks = 0]
     * [pool-1-thread-3] task5 시작 => Queue 가 가득 차면, max 사이즈까 추가 스레드를 생성해서 수행
     * [     main] task5 -> [pool=3, active=3, queuedTasks = 2, completedTasks = 0]
     * [pool-1-thread-4] task6 시작
     * [     main] task6 -> [pool=4, active=4, queuedTasks = 2, completedTasks = 0]
     *
     * [     main] task7 실행 거절 예외 발생: java.util.concurrent.RejectedExecutionException: Task thread.executor.RunnableTask@3abbfa04 rejected from java.util.concurrent.ThreadPoolExecutor@7f690630[Running, pool size = 4, active threads = 4, queued tasks = 2, completed tasks = 0]

     * [pool-1-thread-1] task1 완료
     * [pool-1-thread-2] task2 완료
     * [pool-1-thread-1] task3 완료
     * [pool-1-thread-2] task4 완료
     * [pool-1-thread-3] task5 완료
     * [pool-1-thread-4] task6 완료
     *
     * [     main] == 작업 수행 완료 ==
     * [     main] [pool=4, active=0, queuedTasks=0, completedTasks=6]
     *
     * [     main] == maximumPoolSize 대기 시간 초과 ==
     * [     main] [pool=2, active=0, queuedTasks=0, completedTasks=6]
     *
     * [     main] == shutdown 완료 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=6]
     */
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ExecutorService es = new ThreadPoolExecutor(
                2, 4, 3000, TimeUnit.MILLISECONDS, workQueue);
        printState(es);

        es.execute(new RunnableTask("task1"));
        printState(es, "task1");

        es.execute(new RunnableTask("task2"));
        printState(es, "task2");

        es.execute(new RunnableTask("task3"));
        printState(es, "task3");

        es.execute(new RunnableTask("task4"));
        printState(es, "task4");

        es.execute(new RunnableTask("task5"));
        printState(es, "task5");

        es.execute(new RunnableTask("task6"));
        printState(es, "task6");

        try {
            es.execute(new RunnableTask("task7"));
        } catch (RejectedExecutionException e) {
            log("task7 실행 거절 예외 발생: " + e);
        }

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
        printState(es);
    }
}
