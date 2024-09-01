package thread.cas.spinlock;

import static util.MyLogger.log;

public class SpinLockMain {

    /**
     * [ Thread-2] 락 획득 시도
     * [ Thread-1] 락 획득 시도
     * [ Thread-1] 락 획득 완료
     * [ Thread-2] 락 획득 완료
     * [ Thread-1] 비즈니스 로직 실행
     * [ Thread-2] 비즈니스 로직 실행
     * [ Thread-2] 락 반납 완료
     * [ Thread-1] 락 반납 완료
     */
    public static void main(String[] args) {
        SpinLockBad spinLock = new SpinLockBad();

        Runnable task = () -> {
            spinLock.lock();
            try {
                // critical section
                log("비즈니스 로직 실행");
            } finally {
                spinLock.unlock();
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();
    }
}
