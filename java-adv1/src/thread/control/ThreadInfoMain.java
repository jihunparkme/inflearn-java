package thread.control;

import thread.start.HelloRunnable;

import static util.MyLogger.log;

public class ThreadInfoMain {
    public static void main(String[] args) {
        /** main Thread */
        Thread mainThread = Thread.currentThread();
        /**
         * Thread[#1,main,5,main]
         * - [threadId, threadName, threadPriority, threadGroup]
         */
        log("mainThread = " + mainThread);
        /**
         * 1
         * - 스레드의 고유 식별자를 반환하는 메서드(각 스레드에 대해 유일한 ID) -> ID는 스레드가 생성될 때 할당되며, 직접 지정할 수 없다
         */
        log("mainThread.threadId() = " + mainThread.threadId());
        /**
         * main
         * - 스레드의 이름을 반환하는 메서드
         * - 스레드 ID는 중복되지 않지만, 스레드 이름은 중복될 수 있다.
         */
        log("mainThread.getName() = " + mainThread.getName());
        /**
         * 5
         * - 스레드의 우선순위를 반환하는 메서드
         * - 우선순위는 1(가장 낮음)에서 10(가장 높음)까지의 값으로 설정할 수 있으며, 기본값은 5
         * - setPriority() 메서드를 사용해서 우선순위를 변경 가능
         * - 우선순위는 스레드 스케줄러가 어떤 스레드를 우선 실행할지 결정하는 데 사용. 하지만 실제 실행 순서는 JVM 구현과 운영체제에 따라 달라질 수 있다.
         */
        log("mainThread.getPriority() = " + mainThread.getPriority());
        /**
         * java.lang.ThreadGroup[name=main,maxpri=10]
         * - 스레드가 속한 스레드 그룹을 반환하는 메서드
         * - 스레드 그룹은 스레드를 그룹화하여 관리할 수 있는 기능을 제공
         * - 기본적으로 모든 스레드는 부모 스레드와 동일한 스레드 그룹에 속함
         * - 스레드 그룹은 여러 스레드를 하나의 그룹으로 묶어서 특정 작업(예: 일괄 종료, 우선순위 설정 등)을 수행 가능
         * 부모 스레드(Parent Thread)
         * - 새로운 스레드를 생성하는 스레드를 의미
         * - 스레드는 기본적으로 다른 스레드에 의해 생성. 이러한 생성 관계에서 새로 생성된 스레드는 생성한 스레드를 부모로 간주
         */
        log("mainThread.getThreadGroup() = " + mainThread.getThreadGroup());
        /**
         * RUNNABLE
         * - 스레드의 현재 상태를 반환하는 메서드
         * - 반환되는 값은 Thread.State 열거형에 정의된 상수 중 하나
         *  - NEW: 스레드가 아직 시작되지 않은 상태
         *  - RUNNABLE: 스레드가 실행 중이거나 실행될 준비가 된 상태
         *  - BLOCKED: 스레드가 동기화 락을 기다리는 상태이
         *  - WAITING: 스레드가 다른 스레드의 특정 작업이 완료되기를 기다리는 상태
         *  - TIMED_WAITING: 일정 시간 동안 기다리는 상태
         *  - TERMINATED: 스레드가 실행을 마친 상태
         */
        log("mainThread.getState() = " + mainThread.getState());

        log("==============================");

        // myThread Thread
        Thread myThread = new Thread(new HelloRunnable(), "myThread");
        log("myThread = " + myThread); // Thread[#21,myThread,5,main]
        log("myThread.threadId() = " + myThread.threadId()); // 21
        log("myThread.getName() = " + myThread.getName()); // myThread
        log("myThread.getPriority() = " + myThread.getPriority()); // 5
        log("myThread.getThreadGroup() = " + myThread.getThreadGroup()); // java.lang.ThreadGroup[name=main,maxpri=10]
        log("myThread.getState() = " + myThread.getState()); // NEW
    }
}
