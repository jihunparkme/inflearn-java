package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag); // runFlag = true
        t.start();

        sleep(1000);

        log("runFlag 를 false 로 변경 시도");
        task.runFlag = false;
        log("runFlag = " + task.runFlag); // runFlag = false
        log("main 종료");
    }

    static class MyTask implements Runnable {
        /**
         * volatile 미사용 시
         * 예상과 다르게 task 가 종료되지 않고, 자바 프로그램도 계속 실행
         * work 스레드가 while 문에서 빠져나오지 못하고 있는 상황
         */
        boolean runFlag = true;
        //volatile boolean runFlag = true;

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag 가 false 로 변하면 탈출
            }
            log("task 종료");
        }
    }
}
