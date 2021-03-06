package chap12.WaitNotifyTest;

public class WaitNotifyExample {
    public static void main(String[] args) {
        WorkObject sharedWorkObj = new WorkObject();

        ThreadA threadA = new ThreadA(sharedWorkObj);
        ThreadB threadB = new ThreadB(sharedWorkObj);

        threadA.start();
        threadB.start();
    }
}
// 출력
//ThreadA의 methodB() 작업
//ThreadA의 methodA() 작업
//ThreadA의 methodB() 작업
//ThreadA의 methodA() 작업
//ThreadA의 methodB() 작업
//ThreadA의 methodA() 작업
//ThreadA의 methodB() 작업
//ThreadA의 methodA() 작업
//ThreadA의 methodB() 작업
//ThreadA의 methodA() 작업