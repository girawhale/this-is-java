package chap18.console;

import java.io.Console;

public class ConsoleTest {
    public static void main(String[] args) {
        Console console = System.console();

        System.out.print("ID : ");
        String id = console.readLine();

        System.out.print("PW : ");
        char[] charPass = console.readPassword(); // 화면에 보이지 않는다!!
        String strPass = new String(charPass);

        System.out.println("-------------------");
        System.out.println("ID : " + id);
        System.out.println("PW : " + strPass);
    }
}
