package chap13;

public class BoundedTypeParameterExample {
    public static void main(String[] args) {
//        String str = Util.compare("a", "b"); 에러 발생

        int result1 = Util.compare(10, 20); // int -> Integer 자동 Boxing
        System.out.println(result1);

        int result2 = Util.compare(4.5, 3); // double -> Double 자동 Boxing
        System.out.println(result2);
    }
}

class Util {
    public static <T extends Number> int compare(T t1, T t2) {
        double v1 = t1.doubleValue();
        double v2 = t2.doubleValue();

        return Double.compare(v1, v2);
    }
}
