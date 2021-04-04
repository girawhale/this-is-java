package chap19.TCPNonBlockingChatting;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ServerExample extends Application {
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<>();

    void startServer() { //서버 시작 코드
        try {
            selector = Selector.open(); // Selector 생성
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // 논블로킹 설정
            serverSocketChannel.bind(new InetSocketAddress(5001));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // Selector에 작업 등록

        } catch (IOException e) {
            if (serverSocketChannel.isOpen()) stopServer();
            return;
        }

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    int keyCount = selector.select();
                    if (keyCount == 0) continue;

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectedKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey selectionKey = iter.next();
                        if (selectionKey.isAcceptable()) { // 연결 수락
                            accept(selectionKey);

                        } else if (selectionKey.isReadable()) { // 읽기 작업
                            Client client = (Client) selectionKey.attachment();
                            client.receive(selectionKey);

                        } else if (selectionKey.isWritable()) { // 쓰기 작업
                            Client client = (Client) selectionKey.attachment();
                            client.send(selectionKey);

                        }

                        iter.remove();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Platform.runLater(() -> {
            displayText("[서버 시작]");
            btnStartStop.setText("stop");
        });

    }

    void stopServer() { //서버 종료 코드
        try {
            Iterator<Client> iterator = connections.iterator();

            while (iterator.hasNext()) {
                Client client = iterator.next();
                client.socketChannel.close(); // 연결된 소켓채널 닫기기

                iterator.remove();
            }

            if (serverSocketChannel != null && serverSocketChannel.isOpen())
                serverSocketChannel.close();

            if (selector != null && selector.isOpen())
                selector.close();

            Platform.runLater(() -> {
                displayText("[서버 멈춤]");
                btnStartStop.setText("start");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void accept(SelectionKey selectionKey) { //연결 수락 코드
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept(); // 연결 수락

            String msg = "[연결 수락] : " + socketChannel.getRemoteAddress() + " / "
                    + Thread.currentThread().getName();
            Platform.runLater(() -> displayText(msg));

            Client client = new Client(socketChannel);
            connections.add(client);

            Platform.runLater(() -> displayText("[연결 개수 : " + connections.size() + "]"));

        } catch (IOException e) {
            if (serverSocketChannel.isOpen()) stopServer();
        }
    }

    class Client { //데이터 통신 코드
        SocketChannel socketChannel;
        String sendData; // 클라이언트로 보낼 데이터를 저장

        public Client(SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false); // 넌블로킹 설정
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);// 읽기 작업 등록

            selectionKey.attach(this);// 자기 자신을 첨부
        }

        void receive(SelectionKey selectionKey) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                // 클라이언트가 비정상 종료시 IOException 발생
                int readByteCount = socketChannel.read(byteBuffer); // 데이터 받기

                // 클라이언트가 정상적으로 close() 호출시
                if (readByteCount == -1)
                    throw new IOException();

                String msg = "[요청 처리] " + socketChannel.getRemoteAddress() + " / "
                        + Thread.currentThread().getName();
                Platform.runLater(() -> displayText(msg));

                byteBuffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                String data = charset.decode(byteBuffer).toString();

                for (Client client : connections) { // 모든 클라이언트에게 문자열 전송
                    client.sendData = data;
                    SelectionKey key = client.socketChannel.keyFor(selector);
                    key.interestOps(SelectionKey.OP_WRITE); // 작업 유형 변경
                }
                selector.wakeup(); // 변경된 작업 유형을 감지하도록 블로킹을 해제하고 다시 실행하도록


            } catch (IOException e) {
                try {
                    connections.remove(this);
                    String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
                            + Thread.currentThread().getName() + "]";
                    Platform.runLater(() -> displayText(message));
                    socketChannel.close();
                } catch (IOException e2) {
                }
            }
        }

        void send(SelectionKey selectionKey) {
            try {
                Charset charset = StandardCharsets.UTF_8;
                ByteBuffer byteBuffer = charset.encode(sendData);
                socketChannel.write(byteBuffer); // 데이터 전송

                selectionKey.interestOps(SelectionKey.OP_READ); // 읽기 모드 변경
                selector.wakeup(); // 변경된 작업 유형을 감지하도록 selector의 블로킹 해제

            } catch (IOException e) {
                try {
                    String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
                            + Thread.currentThread().getName() + "]";
                    Platform.runLater(() -> displayText(message));
                    connections.remove(this);
                    socketChannel.close();
                } catch (IOException e2) {
                }
            }
        }

    }


    //////////////////////////////////////
    //UI 생성 코드
    TextArea txtDisplay;
    Button btnStartStop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
        root.setCenter(txtDisplay);

        btnStartStop = new Button("start");
        btnStartStop.setPrefHeight(30);
        btnStartStop.setMaxWidth(Double.MAX_VALUE);
        btnStartStop.setOnAction(e -> {
            if (btnStartStop.getText().equals("start")) {
                startServer();
            } else if (btnStartStop.getText().equals("stop")) {
                stopServer();
            }
        });
        root.setBottom(btnStartStop);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.setOnCloseRequest(event -> stopServer());
        primaryStage.show();
    }

    void displayText(String text) {
        txtDisplay.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
