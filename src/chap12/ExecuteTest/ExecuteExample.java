package chap12.ExecuteTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecuteExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2); // 최대 스레드 개수가 2인 스레드풀을 생성

        for (int i = 0; i < 10; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //스레드 총 개수 및 작업 스레드 이름 출력
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;

                    int poolSize = threadPoolExecutor.getPoolSize();
                    String threadName = Thread.currentThread().getName();
                    System.out.printf("[총 스레드 개수 : %d] 작업 스레드 이름 : %s\n", poolSize, threadName);

                    int value = Integer.parseInt("삼");// 강제 예외 발생
                }
            };

            // 작업 처리 요청
//            executorService.execute(runnable);
            executorService.submit(runnable);
        }

        executorService.shutdown(); // 스레드풀 종료


    }
}
// execute => 스레드가 종료되고 재생성
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-2
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-3
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-4
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-5
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-6
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-7
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-8
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-9
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-10

// submit => 스레드가 재활용
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-2
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-2
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-2
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-2
//[총 스레드 개수 : 2] 작업 스레드 이름 : pool-1-thread-1