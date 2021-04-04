package chap19;

import java.nio.ByteBuffer;

public class CompactExample {
    public static void main(String[] args) {
        System.out.println("[7바이트 버퍼 생성]");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7);

        byteBuffer.put((byte) 10);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 12);
        byteBuffer.put((byte) 13);
        byteBuffer.put((byte) 14);
        byteBuffer.flip(); // 읽기 모드 변경
        printState(byteBuffer);

        byteBuffer.get(new byte[3]);
        System.out.println("[3바이트 읽기]");

        byteBuffer.compact();
        System.out.println("[compact() 실행]");
        printState(byteBuffer);

    }

    private static void printState(ByteBuffer buffer) {
        System.out.print(buffer.get(0) + ", ");
        System.out.print(buffer.get(1) + ", ");
        System.out.print(buffer.get(2) + ", ");
        System.out.print(buffer.get(3) + ", ");
        System.out.println(buffer.get(4));
        System.out.print("position :" + buffer.position() + ", ");
        System.out.print("limit : " + buffer.limit() + ", ");
        System.out.println("capacity :" + buffer.capacity());
    }
}
