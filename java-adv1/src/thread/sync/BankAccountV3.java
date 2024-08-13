package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV3 implements BankAccount {

    private int balance;

    public BankAccountV3(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public synchronized boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        log("[검증 시작] 출금액: " + amount + ", 잔액: " + this.balance);
        if (this.balance < amount) {
            log("[검증 실패] 출금액: " + amount + ", 잔액: " + this.balance);
            return false;
        }

        log("[검증 완료] 출금액: " + amount + ", 잔액: " + this.balance);
        sleep(1000); // 출금에 걸리는 시간으로 가정
        this.balance = this.balance - amount;
        log("[출금 완료] 출금액: " + amount + ", 변경 잔액: " + this.balance);

        log("거래 종료");
        return true;
    }

    @Override
    public synchronized int getBalance() {
        return this.balance;
    }
}
