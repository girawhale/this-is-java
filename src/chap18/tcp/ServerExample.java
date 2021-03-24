package chap18.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServerExample {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            // 5001번 포트에 바인딩 하는 ServerSocket 생성
//         serverSocket = new ServerSocket(5001);
            serverSocket = new ServerSocket();
//        serverSocket.bind(new InetSocketAddress(5001));
            serverSocket.bind(new InetSocketAddress("localhost", 5001)); // 특정 IP의 접속만 허용할 때

            while (true) {
                System.out.println("[연결 대기중...]");
                Socket socket = serverSocket.accept(); //클라이언트와 통신할 Socket을 만들고 리턴
                InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress(); // 연결된 클라이언트의 정보 얻기
                System.out.println("[연결 수락!] : " + socketAddress.getHostName());

                byte[] bytes = null;
                String msg = null;

                InputStream is = socket.getInputStream();
                bytes = new byte[100];
                int readByteCnt = is.read(bytes); // 상대방이 비정상적으로 종료했을 경우 IOException 발생

                if (readByteCnt == -1) // 상대방이 정상 종료했을 때
                    throw new IOException(); //강제로 IOException 발생

                msg = new String(bytes, 0, readByteCnt, StandardCharsets.UTF_8);
                System.out.println("[데이터 받기] : " + msg);

                OutputStream os = socket.getOutputStream();
                msg = "HELLO CLIENT!!";
                bytes = msg.getBytes(StandardCharsets.UTF_8);
                os.write(bytes);
                os.flush();
                System.out.println("[데이터 전송]");

                is.close();
                os.close();
                socket.close();
            }

        } catch (SocketException e) {
        } // accept()에서 블로킹 되어있을 때 ServerSocket을 close()하면 SocketException이 발생

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }

    }
}
