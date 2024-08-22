package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV5 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    private Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV5(int max) {
        this.max = max;
    }

    public void put(String data) {
        lock.lock();
        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                /**
                 * Object.wait()와 유사한 기능
                 * ReentrantLock에서 획득한 락을 반납하고 지정한 condition에 현재 스레드를 WAITING 상태로 보관
                 */
                producerCond.await();
                log("[put] 생산자 깨어남");
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, consumerCond.signal() 호출");
            /**
             * Object.notify()와 유사한 기능
             * 지정한 condition에서 대기중인 스레드를 하나 깨움. 깨어난 스레드는 condition에서 탈출
             */
            consumerCond.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public String take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                consumerCond.await();
                log("[take] 소비자 깨어남");
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, producerCond.signal() 호출");
            producerCond.signal();
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String toString() {
        return queue.toString();
    }
}
