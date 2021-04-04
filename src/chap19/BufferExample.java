package chap19;

import java.nio.ByteBuffer;

public class BufferExample {
    public static void main(String[] args) {
        System.out.println("[7바이트 버퍼 생성]");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7); // 다이렉트 버퍼 생성
        printState(byteBuffer);

        // 상대적 저장
        byteBuffer.put((byte) 10);
        byteBuffer.put((byte) 11);
        System.out.println("[2바이트 저장]");
        printState(byteBuffer);

        // 상대적 저장
        byteBuffer.put((byte) 12);
        byteBuffer.put((byte) 13);
        byteBuffer.put((byte) 14);
        System.out.println("[3바이트 저장]");
        printState(byteBuffer);

        // 읽기모드 변경
        byteBuffer.flip();
        System.out.println("[flip 실행]");
        printState(byteBuffer);

        //상대적 읽기
        byteBuffer.get(new byte[3]);
        System.out.println("[3바이트 읽기]");
        printState(byteBuffer);

        // 마크하기
        byteBuffer.mark();
        System.out.println("[현재 위치 mark]");

        // 상대적 읽기
        byteBuffer.get(new byte[2]);
        System.out.println("[2바이트 읽기]");
        printState(byteBuffer);

        // mark위치로 position 이동
        byteBuffer.reset();
        System.out.println("[position = mark]");
        printState(byteBuffer);

        // position을 0으로 이동
        byteBuffer.rewind();
        System.out.println("[position = 0]");
        printState(byteBuffer);

        // 속성 초기화
        byteBuffer.clear();
        System.out.println("[clear() 실행]");
        printState(byteBuffer);


    }

    private static void printState(ByteBuffer byteBuffer) {
        System.out.print("- position : " + byteBuffer.position() + ", ");
        System.out.print("\tlimit : " + byteBuffer.limit() + ", ");
        System.out.println("\tposition : " + byteBuffer.capacity());
    }
}
