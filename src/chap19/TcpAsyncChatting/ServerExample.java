package chap19.TcpAsyncChatting;

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
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class ServerExample extends Application {
    AsynchronousChannelGroup channelGroup;
    AsynchronousServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<>();

    void startServer() {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    Executors.defaultThreadFactory()
            );
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(5001)); // 이미 바인드 되어있다면 에러발생. stopServer로 정지해야 함

        } catch (IOException e) {
            if (serverSocketChannel.isOpen()) stopServer();
        }
        Platform.runLater(() -> {
            displayText("[서버 시작]");
            btnStartStop.setText("stop");
        });

        serverSocketChannel.accept(null,
                new CompletionHandler<AsynchronousSocketChannel, Void>() {
                    @Override
                    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                        try {
                            String msg = "[연결 수락] : " + socketChannel.getRemoteAddress() + " / "
                                    + Thread.currentThread().getName();
                            Platform.runLater(() -> displayText(msg));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Client client = new Client(socketChannel);
                        connections.add(client);

                        Platform.runLater(() -> displayText("[연결 개수 : " + connections.size() + "]"));

                        serverSocketChannel.accept(null, this); // accept 다시 호출
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        if (serverSocketChannel.isOpen()) stopServer();
                    }
                });

    }

    void stopServer() {
        try {
            connections.clear();
            if (channelGroup != null && !channelGroup.isShutdown())
                channelGroup.shutdown();

            Platform.runLater(() -> {
                displayText("[서버 멈춤]");
                btnStartStop.setText("start");
            });
        } catch (Exception e) {
        }
    }


    class Client {
        AsynchronousSocketChannel socketChannel;

        public Client(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            receive();
        }

        void receive() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            socketChannel.read(byteBuffer, byteBuffer,
                    new CompletionHandler<>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            try {
                                String msg = "[요청 처리] " + socketChannel.getRemoteAddress() + " / "
                                        + Thread.currentThread().getName();
                                Platform.runLater(() -> displayText(msg));

                                attachment.flip();
                                Charset charset = StandardCharsets.UTF_8;
                                String data = charset.decode(attachment).toString();

                                for (Client client : connections)  // 모든 클라이언트에게 문자열 전송
                                    client.send(data);

                                ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                                socketChannel.read(byteBuffer, byteBuffer, this); // 다시 데이터 읽기

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
                                        + Thread.currentThread().getName() + "]";
                                Platform.runLater(() -> displayText(message));
                                connections.remove(Client.this);
                                socketChannel.close();
                            } catch (IOException e) {
                            }
                        }
                    });
        }

        void send(String data) {
            Charset charset = StandardCharsets.UTF_8;
            ByteBuffer byteBuffer = charset.encode(data);

            socketChannel.write(byteBuffer, null,
                    new CompletionHandler<>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            try {
                                String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
                                        + Thread.currentThread().getName() + "]";
                                Platform.runLater(() -> displayText(message));
                                connections.remove(Client.this);
                                socketChannel.close();
                            } catch (IOException e) {
                            }
                        }
                    });


        }
    }

    ///////////////////////////////////////////
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
