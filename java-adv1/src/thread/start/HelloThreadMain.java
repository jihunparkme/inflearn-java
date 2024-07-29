package thread.start;

public class HelloThreadMain {
    /**
     * 스레드는 순서와 실행 기간을 모두 보장하지 않는다.
     */
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() thread start");

        HelloThread helloThread = new HelloThread();
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
        /**
         * 스레드를 실행하는 메서드
         * HelloThread 스레드가 별도의 스레드에서 run() 메서드를 실행
         *
         * main 스레드가 run() 을 호출하는 것이 아니고,
         * main 스레드는 단지 start() 메서드를 통해 Thread-0 스레드에게 실행을 지시
         */
        helloThread.start();
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main() thread end");
    }
}
