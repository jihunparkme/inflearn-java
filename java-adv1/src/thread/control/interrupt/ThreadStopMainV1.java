package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 runFlag=false");
        task.runFlag = false;
    }

    static class MyTask implements Runnable {

        // volatile: 여러 스레드에서 공유하는 값에 사용하는 키워드
        volatile boolean runFlag = true;

        @Override
        public void run() {
            /**
             * 작업 종료를 요청했지만, 작업이 바로 종료되지 않고 작업이 끝난 뒤, 다음 while 문에서 중단되게 된다.
             */
            while (this.runFlag) {
                log("작업 중");
                sleep(3000);
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
