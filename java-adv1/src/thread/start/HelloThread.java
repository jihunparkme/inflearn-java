package thread.start;

public class HelloThread extends Thread {
    /**
     * Thread 클래스를 상속 받아서 스레드를 생성
     */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": run()");
    }
}
