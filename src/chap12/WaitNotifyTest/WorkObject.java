package chap12.WaitNotifyTest;

public class WorkObject {
    public synchronized void methodA() {
        System.out.println("ThreadA의 methodA() 작업");
        notify();

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void methodB() {
        System.out.println("ThreadA의 methodB() 작업");
        notify();

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
