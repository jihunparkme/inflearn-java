package thread.bounded;

/**
 * 버퍼 역할을 하는 큐의 인터페이스
 */
public interface BoundedQueue {
    // 버퍼에 데이터를 보관
    void put(String data);
    // 버퍼에 보관된 값을 가져감
    String take();
}
