package chap19;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileChannelReadExample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/chap19/dir/file.txt");
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        Charset charset = Charset.defaultCharset();
        String data = "";
        int byteCount;

        while ((byteCount = fileChannel.read(byteBuffer)) != -1) { // 최대 100바이트 읽음
            byteBuffer.flip(); // limit을 현재 position으로 설정하고 position을 0으로 설정

            data+=charset.decode(byteBuffer).toString();
            byteBuffer.clear(); // position을 0번 인덱스로 limit를 capacity로 설정해 초기화
        }

        fileChannel.close();

        System.out.println("file.txt의 내용");
        System.out.println(data);
    }
}
