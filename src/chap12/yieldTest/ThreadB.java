package chap12.yieldTest;

public class ThreadB extends Thread {
    public boolean stop = false; // 종료
    public boolean work = true; // 작업 진행 여부

    @Override
    public void run() {
        while (!stop) {
            if (work) 
                System.out.println("THREAD B 작업");
            else
                Thread.yield();
        }
        System.out.println("THREAD B 종료");
    }
}
