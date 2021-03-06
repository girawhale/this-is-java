package chap12.ThreadInfo;

import java.util.Map;
import java.util.Set;

public class ThreadInfoExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true){}
        });
        thread.setName("TempThread");
        thread.setDaemon(true);
        thread.start();

        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        Set<Thread> threads = map.keySet();
        for (Thread t : threads) {
            System.out.println("Name : " + t.getName() + (t.isDaemon() ? "데몬" : "주"));
            System.out.println("\t" + "소속 그룹 : "+t.getThreadGroup().getName());
            System.out.println();
        }
    }
}
//Name : Reference Handler데몬
//	소속 그룹 : system
//
//Name : TempThread데몬
//	소속 그룹 : main
//
//Name : Monitor Ctrl-Break데몬
//	소속 그룹 : main
//
//Name : Common-Cleaner데몬
//	소속 그룹 : InnocuousThreadGroup
//
//Name : Notification Thread데몬
//	소속 그룹 : system
//
//Name : Signal Dispatcher데몬
//	소속 그룹 : system
//
//Name : Finalizer데몬
//	소속 그룹 : system
//
//Name : main주
//	소속 그룹 : main
//
//Name : Attach Listener데몬
//	소속 그룹 : system