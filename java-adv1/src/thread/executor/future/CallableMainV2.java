package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV2 {

    /**
     * [     main] submit() 호출
     * [pool-1-thread-1] Callable 시작
     * [     main] future 즉시 반환, future = java.util.concurrent.FutureTask@46d56d67[Not completed, task = thread.executor.future.CallableMainV2$MyCallable@14acaea5]
     *
     * [     main] future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING
     * [pool-1-thread-1] create value = 5
     * [pool-1-thread-1] Callable 완료
     * [     main] future.get() [블로킹] 메서드 호출 완료 -> , main 스레드 RUNNABLE
     *
     * [     main] result value = 5
     * [     main] future 완료, future = java.util.concurrent.FutureTask@46d56d67[Completed normally]
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);
        log("submit() 호출");
        /**
         * task의 미래 결과를 알 수 있는 Future(실제 구현체는 FutureTask) 객체를 생성하고,
         * task를 감싸고 있는 Future가 블로킹 큐에 담김
         */
        Future<Integer> future = es.submit(new MyCallable());
        log("future 즉시 반환, future = " + future); // Not completed

        log("future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING");
        /**
         * 블로킹 메서드(Thread.join(), Future.get())
         * - 스레드가 작업을 바로 수행하지 않고, 다른 작업이 완료될 때까지 대기
         * - 이러한 메서드를 호출하면 호출한 스레드는 지정된 작업이 완료될 때까지 블록(대기)되어 다른 작업을 수행할 수 없음
         *
         * future.get()
         * - Future가 완료 상태가 될 때까지 대기(RUNNABLE -> WAITING)
         * - 작업이 완료되 요청 스레드를 깨우고, 요청 스레드는 WAITING -> RUNNABLE 상태로 전환
         */
        Integer result = future.get();
        log("future.get() [블로킹] 메서드 호출 완료 -> , main 스레드 RUNNABLE");

        log("result value = " + result);
        log("future 완료, future = " + future); // Completed normally
        es.close();
    }

    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() {
            log("Callable 시작");
            sleep(2000);
            int value = new Random().nextInt(10);
            log("create value = " + value);
            log("Callable 완료");
            return value;
        }
    }
}
