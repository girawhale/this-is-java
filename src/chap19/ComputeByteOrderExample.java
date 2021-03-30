package chap19;

import java.nio.ByteOrder;

public class ComputeByteOrderExample {
    public static void main(String[] args) {
        System.out.println("OS : " + System.getProperty("os.name"));
        System.out.println("Byte Order : " + ByteOrder.nativeOrder());
    }
}
