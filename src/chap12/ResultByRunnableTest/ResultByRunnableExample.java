package chap12.ResultByRunnableTest;

import java.util.concurrent.*;

public class ResultByRunnableExample {
    public static void main(String[] args) {
        System.out.println("현재 가능한 스레드 수: " + Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        System.out.println("[작업 처리 요청]");
        class Task implements Runnable {
            Result result; // 외부 Result 객체를 필드에 저장

            Task(Result result) {
                this.result = result;
            }

            @Override
            public void run() {
                int sum = 0;
                for (int i = 1; i <= 10; i++)
                    sum += i;

                result.addValue(sum); // Result 객체에 작업 결과 저장
            }
        }

        Result result = new Result();
        Runnable task1 = new Task(result);
        Runnable task2 = new Task(result);
        Future<Result> future1 = executorService.submit(task1, result);
        Future<Result> future2 = executorService.submit(task2, result);

        try {
            result = future1.get();
            result = future2.get();
            System.out.println("[처리 결과] : " + result.accumVal);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[실행 예외 발생]" + e.getMessage());
        }

        executorService.shutdown();
    }
}

class Result {
    int accumVal;

    synchronized void addValue(int value) {
        accumVal += value;
    }

}
