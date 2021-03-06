package chap12.JoinTest;

public class JoinExample {
    public static void main(String[] args) {
        SumThread sumThread = new SumThread();
        sumThread.start();

        try {
            sumThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("1~100까지의 합 = " + sumThread.getSum());// join이 없다면 출력이 0
    }
}
