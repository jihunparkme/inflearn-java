package thread.control.print;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinterV1 {

    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner userInput = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");
            String input = userInput.nextLine();
            if (input.equals("q")) {
                printerThread.interrupt();
                break;
            }
            printer.addJob(input);
        }
    }

    /**
     * ConcurrentLinkedQueue
     * - 여러 스레드가 동시에 접근하는 경우, 컬렉션 프레임워크가 제공하는 일반적인 자료구조를 사용하면 위험
     * - 동시성을 지원하는 동시성 컬렉션을 사용
     */
    static class Printer implements Runnable {
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            /**
             * CPU에서 while, if 로직이 계속 반복 수행하여 많은 CPU 자원을 사용
             */
            while (!Thread.interrupted()) {
                if (jobQueue.isEmpty()) {
                    Thread.yield(); // 작업이 비어있으면 다른 스레드에 작업을 양보
                    continue;
                }

                try {
                    String job = jobQueue.poll();
                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
                    Thread.sleep(3000);
                    log("출력 완료: " + job);
                } catch (InterruptedException e) {
                    log("인터럽트 !!!");
                    break;
                }
            }
            log("프린터 종료");
        }

        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}