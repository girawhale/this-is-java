package chap19.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ServerExample {
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(5001));

            while (true) {
                System.out.println("[연결 대기]");
                SocketChannel socketChannel = serverSocketChannel.accept();
                InetSocketAddress isa = (InetSocketAddress) socketChannel.getRemoteAddress();
                System.out.println("[연결 수락] : " + isa.getHostName());

                ByteBuffer buffer = null;
                Charset charset = StandardCharsets.UTF_8;

                buffer = ByteBuffer.allocate(100);
                int byteCount = socketChannel.read(buffer);
                if (byteCount == -1) // 상대방이 정상종료 했을 때
                    throw new IOException(); // 강제로 IOException 발생

                buffer.flip();
                String msg = charset.decode(buffer).toString();
                System.out.println("[데이터 수신] : " + msg);

                buffer = charset.encode("HELLO CLIENT");
                socketChannel.write(buffer);
                System.out.println("[데이터 전송]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serverSocketChannel.isOpen()) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
