package chap18.io;

import java.io.*;

public class FileOutStreamExample {
    public static void main(String[] args) throws IOException {
        String originFileName = "src/chap18/io/test";
        String targetFileName = "src/chap18/io/copy";

        FileInputStream fis = new FileInputStream(originFileName);
        FileOutputStream fos = new FileOutputStream(targetFileName);

        int read;
        byte[] readBytes = new byte[10];
        while ((read = fis.read(readBytes)) != -1) {
            fos.write(readBytes, 0, read);
        }
    }
}
