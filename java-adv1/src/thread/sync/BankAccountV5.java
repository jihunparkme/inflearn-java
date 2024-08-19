package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV5 implements BankAccount {

    private int balance;

    private final Lock lock = new ReentrantLock();

    public BankAccountV5(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        if (!lock.tryLock()) {
            log("[진입 실패] 이미 처리중인 작업이 있습니다.");
            return false;
        }

        try {
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + this.balance);
            if (this.balance < amount) {
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + this.balance);
                return false;
            }

            log("[검증 완료] 출금액: " + amount + ", 잔액: " + this.balance);
            sleep(1000); // 출금에 걸리는 시간으로 가정
            this.balance = this.balance - amount;
            log("[출금 완료] 출금액: " + amount + ", 변경 잔액: " + this.balance);
        } finally {
            /**
             * ReentrantLock 을 활용한 락 해제
             * - 락을 반납하지 않을 경우 대기하는 스레드가 락을 얻지 못함
             */
            lock.unlock();
        }
        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        lock.lock();
        try {
            return this.balance;
        } finally {
            lock.unlock();
        }
    }
}
