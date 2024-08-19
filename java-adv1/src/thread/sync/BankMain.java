package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankMain {

    /**
     * [       t1] 거래 시작: BankAccountV4
     * [       t2] 거래 시작: BankAccountV4
     * [       t1] [검증 시작] 출금액: 800, 잔액: 1000
     * [       t1] [검증 완료] 출금액: 800, 잔액: 1000
     * [     main] t1 state: TIMED_WAITING
     * [     main] t2 state: WAITING
     * [       t1] [출금 완료] 출금액: 800, 변경 잔액: 200
     * [       t1] 거래 종료
     * [       t2] [검증 시작] 출금액: 800, 잔액: 200
     * [       t2] [검증 실패] 출금액: 800, 잔액: 200
     * [     main] 최종 잔액: 200
     */
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccountV4(1000);

        Thread t1 = new Thread(new WithdrawTask(account, 800), "t1");
        Thread t2 = new Thread(new WithdrawTask(account, 800), "t2");

        t1.start();
        t2.start();

        sleep(500);
        log("t1 state: " + t1.getState());
        log("t2 state: " + t2.getState());

        t1.join();
        t2.join();

        log("최종 잔액: " + account.getBalance());
    }
}
