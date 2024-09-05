package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectMainV2 {

    /**
     * DiscardPolicy
     * 거절된 작업을 무시하고 아무런 예외도 발생시키지 않음
     *
     * [pool-1-thread-1] task1 시작
     * [pool-1-thread-1] task1 완료
     */
    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                1, 1,
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy());
        executor.submit(new RunnableTask("task1"));

        executor.submit(new RunnableTask("task1"));
        executor.submit(new RunnableTask("task2"));
        executor.submit(new RunnableTask("task3"));
        executor.close();
    }
}
