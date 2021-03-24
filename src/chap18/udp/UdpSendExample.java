package chap18.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class UdpSendExample {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        System.out.println("[발신 시작]");

        for (int i = 0; i < 100; i++) {
            String data = "메시지" + i;
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

            DatagramPacket packet = new DatagramPacket( // 패킷 생성
                    bytes, // 보낼 데이터
                    bytes.length, // 보내고자하는 항목 수
                    new InetSocketAddress("localhost", 5001) // 수신자 정보
            );

            datagramSocket.send(packet); // 패킷 전송
            System.out.println("[보낸 바이트 수] : " + bytes.length + "byte");
        }

        System.out.println("[발신 종료]");

        datagramSocket.close();
    }
}
