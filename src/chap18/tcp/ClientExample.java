package chap18.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientExample {
    public static void main(String[] args) throws Exception {
        Socket socket = null;

        try {
            System.out.println("[연결 요청]");
//            socket = new Socket("localhost", 5001);
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 5001)); // 도메인 이름을 사용하려면 이 방법
            System.out.println("[연결 성공]");

            byte[] bytes = null;
            String msg = null;

            OutputStream os = socket.getOutputStream();
            msg = "HELLO SERVER?";
            bytes = msg.getBytes(StandardCharsets.UTF_8);
            os.write(bytes);
            os.flush();
            System.out.println("[데이터 전송]");

            InputStream is = socket.getInputStream();
            bytes = new byte[100];
            int readByteCnt = is.read(bytes);
            String data = new String(bytes, 0, readByteCnt, StandardCharsets.UTF_8);
            System.out.println("[데이터 받기] : " + data);

            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
