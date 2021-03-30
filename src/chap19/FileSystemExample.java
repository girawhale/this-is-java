package chap19;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileSystemExample {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystems.getDefault();

        System.out.println("FILE_STORE");
        for (FileStore store : fileSystem.getFileStores()) {
            System.out.println("드라이버 명: " + store.name());
            System.out.println("파일 시스템: " + store.type());

            System.out.println("전체 공간: " + store.getTotalSpace() + "btye");
            System.out.println("사용 중인 공간: " + store.getUnallocatedSpace() + "btye");
            System.out.println("사용 가능한 공간: " + store.getUsableSpace() + "btye");

            System.out.println();
        }

        System.out.println("ROOT_DIRECTORY");
        for (Path path : fileSystem.getRootDirectories()) {
            System.out.println(path.toString());
        }

        System.out.println();
        System.out.println("파일 구분자: " + fileSystem.getSeparator());
    }
}
