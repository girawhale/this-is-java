package chap19;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class ByteBufferToIntBufferExample {
    public static void main(String[] args) {
        // int[] -> IntBuffer -> ByteBuffer
        int[] writeData = {10, 20, 30};
        IntBuffer writeIntBuffer = IntBuffer.wrap(writeData);
        ByteBuffer writeByteBuffer = ByteBuffer.allocateDirect(writeIntBuffer.capacity() * 4);

        for (int i = 0; i < writeIntBuffer.capacity(); i++)
            writeByteBuffer.putInt(writeIntBuffer.get(i));
        writeByteBuffer.flip();

        // ByteBuffer -> IntBuffer -> int[]
        ByteBuffer readByteBuffer = writeByteBuffer;
        IntBuffer readIntBuffer = readByteBuffer.asIntBuffer();
//        System.out.println("ReadIntBuffer : " + Arrays.toString(readIntBuffer.array())); // 래핑된 배열만 리턴하므로 사용 불가

        int[] readData = new int[readIntBuffer.capacity()];
        readIntBuffer.get(readData);
        System.out.println("배열 복원 : " + Arrays.toString(readData));

        System.out.println("***********************************");

        ByteBuffer writeByteBuffer2 = ByteBuffer.allocateDirect(writeData.length * 4);

        for (int i = 0; i < writeIntBuffer.capacity(); i++)
            writeByteBuffer2.putInt(writeData[i]);
        writeByteBuffer2.flip();

        // ByteBuffer -> IntBuffer -> int[]
        ByteBuffer readByteBuffer2 = writeByteBuffer2;
        IntBuffer readIntBuffer2 = readByteBuffer2.asIntBuffer();

        int[] readData2 = new int[readIntBuffer2.capacity()];
        readIntBuffer2.get(readData2);
        System.out.println("배열 복원 : " + Arrays.toString(readData2));

    }
}
