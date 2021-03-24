package chap18.io;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamExample {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("src/chap18/io/test");

            int data;
            while ((data = fis.read()) != -1) {
                System.out.write(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
