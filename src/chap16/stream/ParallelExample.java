package chap16.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelExample {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> list.add(i));

        System.out.println("STREAM");
        list.stream().forEach(ParallelExample::print);
        System.out.println();

        System.out.println("PARALLEL");
        list.parallelStream().forEach(ParallelExample::print);
    }

    public static void print(Integer i) {
        System.out.println(i + " : " + Thread.currentThread().getName());
    }
}
//STREAM
//0 : main
//1 : main
//2 : main
//3 : main
//4 : main
//
//PARALLEL
//2 : main
//3 : ForkJoinPool.commonPool-worker-3
//0 : ForkJoinPool.commonPool-worker-3
//4 : main
//1 : ForkJoinPool.commonPool-worker-5