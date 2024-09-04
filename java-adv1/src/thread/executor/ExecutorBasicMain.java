package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ExecutorBasicMain {

    /**
     * [     main] == 초기 상태 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=0]
     *
     * [     main] == 작업 수행 중 ==
     * [     main] [pool=2, active=2, queuedTasks=2, completedTasks=0]
     * [pool-1-thread-2] taskB 시작
     * [pool-1-thread-1] taskA 시작
     * [pool-1-thread-1] taskA 완료
     * [pool-1-thread-2] taskB 완료
     * [pool-1-thread-2] taskD 시작
     * [pool-1-thread-1] taskC 시작
     * [pool-1-thread-2] taskD 완료
     * [pool-1-thread-1] taskC 완료
     *
     * [     main] == 작업 수행 완료 ==
     * [     main] [pool=2, active=0, queuedTasks=0, completedTasks=4]
     *
     * [     main] == shutdown 완료 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=4]
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = new ThreadPoolExecutor(
                2, // corePoolSize : 스레드 풀에서 관리되는 기본 스레드의 수
                2, // maximumPoolSize : 스레드 풀에서 관리되는 최대 스레드 수
                0, TimeUnit.MILLISECONDS, // keepAliveTime , TimeUnit unit : 기본 스레드 수를 초과해서 만들어진 스레드가 생존할 수 있는 대기 시간(이 시간 동안 처리할 작업이 없다면 초과 스레드는 제거)
                new LinkedBlockingQueue<>() // BlockingQueue workQueue : 작업을 보관할 블로킹 큐
        );
        log("== 초기 상태 ==");
        printState(es);

        es.execute(new RunnableTask("taskA"));
        es.execute(new RunnableTask("taskB"));
        es.execute(new RunnableTask("taskC"));
        es.execute(new RunnableTask("taskD"));
        log("== 작업 수행 중 ==");
        printState(es);

        sleep(3000);

        log("== 작업 수행 완료 ==");
        printState(es);
        es.close();

        log("== shutdown 완료 ==");
        printState(es);
    }
}
