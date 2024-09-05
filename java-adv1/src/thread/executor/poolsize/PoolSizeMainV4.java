package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

public class PoolSizeMainV4 {
    /** 일반적인 상황에는 CPU, 메모리 자원을 예측할 수 있도록 고정 크기의 스레드로 서비스를 안정적으로 운영 (100개의 기본 스레드가 처리) */
    static final int TASK_SIZE = 1100; // 1. 일반
    /** 사용자의 요청이 갑자기 증가하면 긴급하게 스레드를 추가로 투입해서 작업을 빠르게 처리 (100개의 기본 스레드 + 100개의 초과 스레드가 처리) */
    //static final int TASK_SIZE = 1200; // 2. 긴급
    /** 사용자의 요청이 폭증해서 긴급 대응도 어렵다면 사용자의 요청을 거절 */
    //static final int TASK_SIZE = 1201; // 3. 거절

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = new ThreadPoolExecutor(
                100, 200, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        printState(es);

        long startMs = System.currentTimeMillis();
        for (int i = 1; i <= TASK_SIZE; i++) {
            String taskName = "task" + i;
            try {
                es.execute(new RunnableTask(taskName));
                printState(es, taskName);
            } catch (RejectedExecutionException e) {
                log(taskName + " -> " + e);
            }
        }

        es.close();
        long endMs = System.currentTimeMillis();
        log("time: " + (endMs - startMs));
    }
}
