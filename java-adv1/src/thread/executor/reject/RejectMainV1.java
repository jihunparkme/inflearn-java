package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class RejectMainV1 {

    /** AbortPolicy (기본 정책)
     * 작업이 거절되면 RejectedExecutionException 발생
     *
     * [pool-1-thread-1] task1 시작
     * [     main] 요청 초과
     * [     main] java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@73f792cf[Not completed, task = java.util.concurrent.Executors$RunnableAdapter@6b2fad11[Wrapped task = thread.executor.RunnableTask@79698539]] rejected from java.util.concurrent.ThreadPoolExecutor@566776ad[Running, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
     * [pool-1-thread-1] task1 완료
     */
    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                1, 1,
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
        executor.submit(new RunnableTask("task1"));

        try {
            executor.submit(new RunnableTask("task2"));
        } catch (RejectedExecutionException e) {
            log("요청 초과");
            // 포기, 다시 시도 등 다양한 고민을 하면 됨
            log(e);
        }

        executor.close();
    }
}
