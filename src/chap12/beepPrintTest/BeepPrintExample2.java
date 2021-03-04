package chap12.beepPrintTest;

import java.awt.*;

public class BeepPrintExample2 {
    public static void main(String[] args) {
        Thread beepThread = new Thread() {
            @Override
            public void run() {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                for (int i = 0; i < 5; i++) {
                    toolkit.beep();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

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
