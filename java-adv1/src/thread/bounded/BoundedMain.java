package thread.bounded;

import java.util.ArrayList;
import java.util.List;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedMain {

    public static void main(String[] args) {
        // 1. BoundedQueue 선택
        BoundedQueue queue = new BoundedQueueV6_1(2);

        // 2. 생산자, 소비자 실행 순서 선택, 반드시 하나만 선택!
        /**
         * [     main] == [생산자 먼저 실행] 시작, BoundedQueueV6_1 ==
         *
         * [     main] 생산자 시작
         * [producer1] [생산 시도] data1 -> []
         * [producer1] [생산 완료] data1 -> [data1]
         * [producer2] [생산 시도] data2 -> [data1]
         * [producer2] [생산 완료] data2 -> [data1, data2]
         * [producer3] [생산 시도] data3 -> [data1, data2]
         *
         * [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
         * [     main] producer1: TERMINATED
         * [     main] producer2: TERMINATED
         * [     main] producer3: WAITING
         *
         * [     main] 소비자 시작
         * [consumer1] [소비 시도] ? <- [data1, data2]
         * [producer3] [생산 완료] data3 -> [data2, data3]
         * [consumer1] [소비 완료] data1 <- [data2]
         * [consumer2] [소비 시도] ? <- [data2, data3]
         * [consumer2] [소비 완료] data2 <- [data3]
         * [consumer3] [소비 시도] ? <- [data3]
         * [consumer3] [소비 완료] data3 <- []
         *
         * [     main] 현재 상태 출력, 큐 데이터: []
         * [     main] producer1: TERMINATED
         * [     main] producer2: TERMINATED
         * [     main] producer3: TERMINATED
         * [     main] consumer1: TERMINATED
         * [     main] consumer2: TERMINATED
         * [     main] consumer3: TERMINATED
         *
         * [     main] == [생산자 먼저 실행] 종료, BoundedQueueV6_1 ==
         */
//        producerFirst(queue); // 생산자 먼저 실행
        /**
         * [     main] == [소비자 먼저 실행] 시작, BoundedQueueV6_1 ==
         *
         * [     main] 소비자 시작
         * [consumer1] [소비 시도] ? <- []
         * [consumer2] [소비 시도] ? <- []
         * [consumer3] [소비 시도] ? <- []
         *
         * [     main] 현재 상태 출력, 큐 데이터: []
         * [     main] consumer1: WAITING
         * [     main] consumer2: WAITING
         * [     main] consumer3: WAITING
         *
         * [     main] 생산자 시작
         * [producer1] [생산 시도] data1 -> []
         * [producer1] [생산 완료] data1 -> [data1]
         * [consumer1] [소비 완료] data1 <- []
         * [producer2] [생산 시도] data2 -> []
         * [producer2] [생산 완료] data2 -> [data2]
         * [consumer2] [소비 완료] data2 <- []
         * [producer3] [생산 시도] data3 -> []
         * [producer3] [생산 완료] data3 -> [data3]
         * [consumer3] [소비 완료] data3 <- []
         *
         * [     main] 현재 상태 출력, 큐 데이터: []
         * [     main] consumer1: TERMINATED
         * [     main] consumer2: TERMINATED
         * [     main] consumer3: TERMINATED
         * [     main] producer1: TERMINATED
         * [     main] producer2: TERMINATED
         * [     main] producer3: TERMINATED
         *
         * [     main] == [소비자 먼저 실행] 종료, BoundedQueueV6_1 ==
         */
        consumerFirst(queue); // 소비자 먼저 실행
    }

    private static void producerFirst(BoundedQueue queue) {
        log("== [생산자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + " == ");

        List<Thread> threads = new ArrayList<>();
        startProducer(queue, threads);
        printAllState(queue, threads);
        startConsumer(queue, threads);
        printAllState(queue, threads);
        log("== [생산자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + " == ");
    }

    private static void consumerFirst(BoundedQueue queue) {
        log("== [소비자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + " == ");

        List<Thread> threads = new ArrayList<>();
        startConsumer(queue, threads);
        printAllState(queue, threads);
        startProducer(queue, threads);
        printAllState(queue, threads);
        log("== [소비자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + " == ");
    }

    private static void startProducer(BoundedQueue queue, List<Thread> threads) {
        System.out.println();
        log("생산자 시작");
        for (int i = 1; i <= 3; i++) {
            Thread producer = new Thread(new ProducerTask(queue, "data" + i), "producer" + i);
            threads.add(producer);
            producer.start();
            sleep(100);
        }
    }

    private static void startConsumer(BoundedQueue queue, List<Thread> threads) {
        System.out.println();
        log("소비자 시작");
        for (int i = 1; i <= 3; i++) {
            Thread consumer = new Thread(new ConsumerTask(queue), "consumer" + i);
            threads.add(consumer);
            consumer.start();
            sleep(100);
        }
    }

    private static void printAllState(BoundedQueue queue, List<Thread> threads) {
        System.out.println();
        log("현재 상태 출력, 큐 데이터: " + queue);
        for (Thread thread : threads) {
            log(thread.getName() + ": " + thread.getState());
        }
    }
}
