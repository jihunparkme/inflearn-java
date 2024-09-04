package thread.executor.future;

import java.util.Random;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class RunnableMain {

    /**
     * [ Thread-1] Runnable 시작
     * [ Thread-1] create value = 3
     * [ Thread-1] Runnable 완료
     * [     main] result value = 3
     */
    public static void main(String[] args) throws InterruptedException {
        MyRunnable task = new MyRunnable();
        Thread thread = new Thread(task, "Thread-1");
        thread.start();
        thread.join();
        // Runnable은 반환값이 없으므로 결과를 멤버 변수에 넣어두고,
        // 스레드가 종료되길 기다린 후 멤버 변수를 통해 값을 확인
        int result = task.value;
        log("result value = " + result);
    }

    static class MyRunnable implements Runnable {
        int value;

        @Override
        public void run() {
            log("Runnable 시작");
            sleep(2000);
            value = new Random().nextInt(10);
            log("create value = " + value);
            log("Runnable 완료");
        }
    }
}
