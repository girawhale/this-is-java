package chap12.yieldTest;

public class YieldExample {
    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB();

        threadA.start();
        threadB.start();

        sleep();
        threadA.work = false;

        sleep();
        threadA.work = true;

        sleep();
        threadA.stop = true;
        threadB.stop = true;
    }

    static void sleep() {
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
