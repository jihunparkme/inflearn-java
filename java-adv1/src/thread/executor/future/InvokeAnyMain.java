package thread.executor.future;

import thread.executor.CallableTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

public class InvokeAnyMain {

    /**
     * [pool-1-thread-2] task2 실행
     * [pool-1-thread-3] task3 실행
     * [pool-1-thread-1] task1 실행
     * [pool-1-thread-1] task1 완료, return = 1000
     * [pool-1-thread-3] 인터럽트 발생, sleep interrupted
     * [pool-1-thread-2] 인터럽트 발생, sleep interrupted
     * [     main] value = 1000
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        CallableTask taskA = new CallableTask("task1", 1000);
        CallableTask taskB = new CallableTask("task2", 2000);
        CallableTask taskC = new CallableTask("task3", 3000);

        List<CallableTask> tasks = List.of(taskA, taskB, taskC);
        /**
         * 지정된 시간 내에 하나의 Callable 작업이 완료될 때까지 대기히고, 가장 먼저 완료된 작업의 결과를 반환
         * 완료되지 않은 나머지 작업은 취소
         */
        Integer value = es.invokeAny(tasks);
        log("value = " + value);
        es.close();
    }
}
