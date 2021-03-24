package chap18.io;

import java.io.*;

public class BufferedOutputStreamExample {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        int data = -1;
        long start = 0;
        long end = 0;

        fis = new FileInputStream("src/chap18/io/img.jpg");
        bis = new BufferedInputStream(fis);
        fos = new FileOutputStream("src/chap18/io/copyImg.jpg");
        start = System.currentTimeMillis();
        while ((data = bis.read()) != -1)
            fos.write(data);

        fos.flush();
        end = System.currentTimeMillis();
        fos.close();
        bis.close();
        fis.close();
        System.out.println("보조스트림 사용 ❌ : " + (end - start) + "ms");


        fis = new FileInputStream("src/chap18/io/img.jpg");
        bis = new BufferedInputStream(fis);
        fos = new FileOutputStream("src/chap18/io/copyImg.jpg");
        bos = new BufferedOutputStream(fos);
        start = System.currentTimeMillis();
        while ((data = bis.read()) != -1)
            bos.write(data);

        bos.flush();
        end = System.currentTimeMillis();
        bos.close();
        fos.close();
        bis.close();
        fis.close();
        System.out.println("보조스트림 사용 ⭕ : " + (end - start) + "ms");
    }
}

//보조스트림 사용❌ : 5568ms
//보조스트림 사용⭕ : 75ms
