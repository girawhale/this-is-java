package chap16.stream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FromFileContentAndDirectoryExample {
    public static void main(String[] args) throws IOException {
        System.out.println("파일 스트림");
        Files.lines(Paths.get("src/chap16/stream/sample.txt"), Charset.defaultCharset())
                .forEach(System.out::println);

        System.out.println();
        System.out.println("디렉토리 스트림");
        Files.list(Paths.get("src")).forEach(System.out::println);
    }
}
