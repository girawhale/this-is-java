package chap19.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientExample {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);

            System.out.println("[연결 요청]");
            socketChannel.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("[연결 성공]");

            ByteBuffer buffer = null;
            Charset charset = StandardCharsets.UTF_8;

            buffer = charset.encode("HELLO SERVER~");
            socketChannel.write(buffer);
            System.out.println("[데이터 전송]");

            buffer = ByteBuffer.allocate(100);
            int byteCount = socketChannel.read(buffer);
            if (byteCount == -1) // 상대방이 정상종료 했을 때
                throw new IOException(); // 강제로 IOException 발생

            buffer.flip();
            String msg = charset.decode(buffer).toString();
            System.out.println("[데이터 수신] : " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(socketChannel.isOpen()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
