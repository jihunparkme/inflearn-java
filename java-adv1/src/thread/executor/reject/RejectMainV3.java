package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectMainV3 {

    /**
     * CallerRunsPolicy
     * - 호출한 스레드가 직접 작업을 수행
     * - 새로운 작업을 제출하는 스레드의 속도가 느려질 수 있음
     *
     * [pool-1-thread-1] task1 시작
     * [     main] task2 시작
     * [pool-1-thread-1] task1 완료
     * [     main] task2 완료
     * [     main] task3 시작
     * [     main] task3 완료
     * [pool-1-thread-1] task4 시작
     * [pool-1-thread-1] task4 완료
     */
    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                1, 1,
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.submit(new RunnableTask("task1"));
        executor.submit(new RunnableTask("task2"));
        executor.submit(new RunnableTask("task3"));
        executor.submit(new RunnableTask("task4"));
        executor.close();
    }
}
