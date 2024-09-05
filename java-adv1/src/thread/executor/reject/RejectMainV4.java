package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;

public class RejectMainV4 {

    /**
     * 사용자 정의
     * - 사용자는 RejectedExecutionHandler 인터페이스를 구현하여 자신만의 거절 처리 전략을 정의
     * - 이를 통해 특정 요구사항에 맞는 작업 거절 방식을 설정
     *
     * [pool-1-thread-1] task1 시작
     * [     main] [경고] 거절된 누적 작업 수: 1
     * [     main] [경고] 거절된 누적 작업 수: 2
     * [     main] [경고] 거절된 누적 작업 수: 3
     * [pool-1-thread-1] task1 완료
     */
    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                1, 1,
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new MyRejectedExecutionHandler());

        executor.submit(new RunnableTask("task1"));
        executor.submit(new RunnableTask("task2"));
        executor.submit(new RunnableTask("task3"));
        executor.submit(new RunnableTask("task4"));
        executor.close();
    }

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        static AtomicInteger count = new AtomicInteger(0);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            int i = count.incrementAndGet();
            log("[경고] 거절된 누적 작업 수: " + i);
        }
    }
}
