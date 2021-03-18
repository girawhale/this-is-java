package chap16.flat;

import java.util.Arrays;
import java.util.List;

public class FlatMapExample {
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("동해물과 백두산이", "마르고 닳도록");
        list1.stream().flatMap(data -> Arrays.stream(data.split(" ")))
                .forEach(System.out::println);

        List<String> list2 = Arrays.asList("1,2,3,4", "5,6,7");
        list2.stream().flatMapToInt(data -> {
            String[] arr = data.split(",");
            int[] ret = new int[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = Integer.parseInt(arr[i]);

            return Arrays.stream(ret);
        }).forEach(System.out::println);
    }
}
