package chap16.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelTimeExample {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        IntStream.range(0, 1_000_000).forEach(i -> list.add(i));

        long time = System.currentTimeMillis();
        System.out.println("STREAM SUM : " + list.stream().reduce(Integer::sum).get());
        System.out.println(System.currentTimeMillis() - time + "ms");

        time = System.currentTimeMillis();
        System.out.println("PARALLEL SUM : " + list.parallelStream().reduce(Integer::sum).get());
        System.out.println(System.currentTimeMillis() - time + "ms");
    }
}
//STREAM SUM : 1783293664
//54ms
//PARALLEL SUM : 1783293664
//19ms