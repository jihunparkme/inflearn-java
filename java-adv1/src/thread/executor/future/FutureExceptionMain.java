package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class FutureExceptionMain {

    /**
     * [     main] 작업 전달
     * [pool-1-thread-1] Callable 실행, 예외 발생
     * [     main] future.get() 호출 시도, future.state(): FAILED
     * [     main] e = java.util.concurrent.ExecutionException: java.lang.IllegalStateException: ex!
     * [     main] cause = java.lang.IllegalStateException: ex!
     */
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);
        log("작업 전달");
        Future<Integer> future = es.submit(new ExCallable());
        sleep(1000);

        try {
            log("future.get() 호출 시도, future.state(): " + future.state());
            Integer result = future.get();
            log("result value = " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            log("e = " + e);
            log("cause = " + e.getCause());  // 원본 예외
        }

        es.close();
    }

    static class ExCallable implements Callable<Integer> {
        @Override
        public Integer call() {
            log("Callable 실행, 예외 발생");
            throw new IllegalStateException("ex!");
        }
    }
}
