package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMain {

    /**
     * volatile 키워드 미사용 시 시점의 차이
     *
     * [     work] flag = true, count = 100000000 in while ()
     * [     work] flag = true, count = 200000000 in while ()
     * [     work] flag = true, count = 300000000 in while ()
     * [     work] flag = true, count = 400000000 in while ()
     * [     work] flag = true, count = 500000000 in while ()
     * [     work] flag = true, count = 600000000 in while ()
     * [     work] flag = true, count = 700000000 in while ()
     * [     work] flag = true, count = 800000000 in while ()
     * [     main] flag = false, count = 844394649 in main
     * [     work] flag = true, count = 900000000 in while ()
     * [     work] flag = false, count = 900000000 종료
     *
     * main 스레드가 flag 값을 false 로 변경한 시점에 count 값 = 844394649
     * work 스레드가 flag 값을 false 로 확인한 시점에 count 값 = 900000000
     *
     * 결과적으로 main 스레드가 flag 값을 false 로 변경하고 한참이 지나서야
     * work 스레드는 flag 값이 false 로 변경된 것을 확인
     */
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        t.start();

        sleep(1000);

        task.flag = false;
        log("flag = " + task.flag + ", count = " + task.count + " in main");
    }

    /**
     * volatile 키워드 적용 시
     *
     * [     work] flag = true, count = 100000000 in while ()
     * [     main] flag = false, count = 103590849 in main
     * [     work] flag = false, count = 103590849 종료
     *
     * main 스레드가 flag 를 변경하는 시점에 work 스레드도 flag 의 변경 값을 정확하게 확인
     */
    static class MyTask implements Runnable {

        // boolean flag = true;
        // long count;
        volatile boolean flag = true;
        volatile long count;

        @Override
        public void run() {
            while (flag) {
                count++;
                //1억번에 한번씩 출력
                if (count % 100_000_000 == 0) {
                    //주석 처리 한다면...
                    log("flag = " + flag + ", count = " + count + " in while () ");
                }
            }
            log("flag = " + flag + ", count = " + count + " 종료");
        }
    }
}