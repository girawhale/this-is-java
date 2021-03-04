package chap12.beepPrintTest;

import java.awt.*;

public class BeepPrintExample {
    public static void main(String[] args) {
        Thread beepThread = new Thread(() -> {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            for (int i = 0; i < 5; i++) {
                toolkit.beep();
                try {
                    Thread.sleep(500); //0.5초 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        beepThread.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("띵!!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
