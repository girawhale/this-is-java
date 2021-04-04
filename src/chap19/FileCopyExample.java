package chap19;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class FileCopyExample {
    public static void main(String[] args) throws IOException {
        Path from = Paths.get("src/chap19/dir/img.jpeg");
        Path to = Paths.get("src/chap19/dir/copy_img.jpeg");

        System.out.println("== FileChannel ==");
        long start = System.currentTimeMillis();
        FileChannel fileChannel_from = FileChannel.open(from, StandardOpenOption.READ);
        FileChannel fileChannel_to = FileChannel.open(to,
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        int byteCount;

        while (true) {
            byteBuffer.clear();
            byteCount = fileChannel_from.read(byteBuffer);
            if (byteCount == -1) break;

            byteBuffer.flip();
            fileChannel_to.write(byteBuffer);
        }

        fileChannel_from.close();
        fileChannel_from.close();
        System.out.println("완료 : " + (System.currentTimeMillis() - start) + "ms");

        System.out.println("== Copy Method ==");
        start = System.currentTimeMillis();

        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("완료 : " + (System.currentTimeMillis() - start) + "ms");
    }
}
