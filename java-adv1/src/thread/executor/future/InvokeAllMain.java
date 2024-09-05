package thread.executor.future;

import thread.executor.CallableTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static util.MyLogger.log;

public class InvokeAllMain {

    /**
     * [pool-1-thread-3] task3 실행
     * [pool-1-thread-1] task1 실행
     * [pool-1-thread-2] task2 실행
     * [pool-1-thread-1] task1 완료, return = 1000
     * [pool-1-thread-2] task2 완료, return = 2000
     * [pool-1-thread-3] task3 완료, return = 3000
     * [     main] value = 1000
     * [     main] value = 2000
     * [     main] value = 3000
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        CallableTask task1 = new CallableTask("task1", 1000);
        CallableTask task2 = new CallableTask("task2", 2000);
        CallableTask task3 = new CallableTask("task3", 3000);

        List<CallableTask> tasks = List.of(task1, task2, task3);
        // 모든 Callable 작업을 제출하고, 모든 작업이 완료될 때까지 대기
        List<Future<Integer>> futures = es.invokeAll(tasks);
        for (Future<Integer> future : futures) {
            Integer value = future.get();
            log("value = " + value);
        }
        es.close();
    }
}
