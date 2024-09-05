package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class PoolSizeMainV3 {

    /** 캐시 풀 전략
     * > 특징
     * - 매우 빠르고, 유연한 전략
     * - 기본 스레드도 없고, 대기 큐에 작업도 쌓이지 않음
     * - 대신에 작업 요청이 오면 초과 스레드로 작업을 바로바로 처리
     * - 따라서 빠른 처리가 가능하고,. 초과 스레드의 수도 제한이 없기 때문에 CPU, 메모리 자원만 허용한다면 시스템의 자원을 최대로 사용 가능
     * - 추가로 초과 스레드는 60초간 생존하기 때문에 작업 수에 맞추어 적절한 수의 스레드가 재사용
     * - 이런 특징으로 요청이 갑자기 증가하면 스레드도 갑자기 증가하고, 요청이 줄어들면 스레드도 점점 줄어든다.
     * - 이 전략은 작업의 요청 수에 따라서 스레드도 증가하고 감소하므로, 매우 유연한 전략
     *
     * > 주의
     * - 점진적인 사용자 확대 (CPU 사용량 초과, OOM 으로 시스템 다운)
     * - 갑작스런 요청 증가 (CPU 사용량 초과, OOM 으로 시스템 다운)
     *
     * [     main] pool 생성
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=0]
     * [pool-1-thread-1] task1 시작
     * [     main] task1 -> [pool=1, active=1, queuedTasks = 0, completedTasks = 0]
     * [     main] task2 -> [pool=2, active=2, queuedTasks = 0, completedTasks = 0]
     * [pool-1-thread-2] task2 시작
     * [     main] task3 -> [pool=3, active=3, queuedTasks = 0, completedTasks = 0]
     * [pool-1-thread-3] task3 시작
     * [     main] task4 -> [pool=4, active=4, queuedTasks = 0, completedTasks = 0]
     * [pool-1-thread-4] task4 시작
     * [pool-1-thread-1] task1 완료
     * [pool-1-thread-4] task4 완료
     * [pool-1-thread-2] task2 완료
     * [pool-1-thread-3] task3 완료
     * [     main] == 작업 수행 완료 ==
     * [     main] [pool=4, active=0, queuedTasks=0, completedTasks=4]
     * [     main] == maximumPoolSize 대기 시간 초과 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=4]
     * [     main] == shutdown 완료 ==
     * [     main] [pool=0, active=0, queuedTasks=0, completedTasks=4]
     */
    public static void main(String[] args) throws InterruptedException {
        // ExecutorService es = Executors.newCachedThreadPool();
        // keepAliveTime 60초 -> 3초로 조절
        ThreadPoolExecutor es = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE, 3, TimeUnit.SECONDS, new SynchronousQueue<>());

        log("pool 생성");
        printState(es);

        for (int i = 1; i <= 4; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName);
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
