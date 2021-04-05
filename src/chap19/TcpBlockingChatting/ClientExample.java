package chap19.TcpBlockingChatting;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientExample extends Application {
    SocketChannel socketChannel;

    void startClient() {
        Thread thread = new Thread(() -> {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(true);
                socketChannel.connect(new InetSocketAddress("localhost", 5001));

                Platform.runLater(() -> {
                    try {
                        displayText("[연결 완료] " + socketChannel.getRemoteAddress());
                        btnConn.setText("stop");
                        btnSend.setDisable(false);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                Platform.runLater(() -> displayText("[서버 통신 안됨]"));
                if (socketChannel.isOpen()) {
                    stopClient();
                }
                return;
            }
            receive();
        });

        thread.start();
    }

    void stopClient() {
        try {
            Platform.runLater(() -> {
                displayText("[연결 끊음]");
                btnConn.setText("start");
                btnSend.setDisable(true);
            });
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
        }
    }

    void receive() {
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                //서버가 비정상적으로 종료시 IOException 발생
                int readByteCount = socketChannel.read(byteBuffer);

                //서버가 정상적으로 Socket의 close()를 호출했을 경우
                if (readByteCount == -1) {
                    throw new IOException();
                }

                byteBuffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                String data = charset.decode(byteBuffer).toString();

                Platform.runLater(() -> displayText("[받기 완료] " + data));
            } catch (Exception e) {
                Platform.runLater(() -> displayText("[서버 통신 안됨]"));
                stopClient();
                break;
            }
        }
    }

    void send(String data) {
        Thread thread = new Thread(() -> {
            try {
                Charset charset = StandardCharsets.UTF_8;
                ByteBuffer byteBuffer = charset.encode(data);
                socketChannel.write(byteBuffer);
                Platform.runLater(() -> displayText("[보내기 완료]"));
            } catch (Exception e) {
                Platform.runLater(() -> displayText("[서버 통신 안됨]"));
                stopClient();
            }
        });
        thread.start();
    }


    //////////////////////////////////////////////////////
    // UI 코드
    TextArea txtDisplay;
    TextField txtInput;
    Button btnConn, btnSend;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
        root.setCenter(txtDisplay);

        BorderPane bottom = new BorderPane();
        txtInput = new TextField();
        txtInput.setPrefSize(60, 30);
        BorderPane.setMargin(txtInput, new Insets(0, 1, 1, 1));

        btnConn = new Button("start");
        btnConn.setPrefSize(60, 30);
        btnConn.setOnAction(e -> {
            if (btnConn.getText().equals("start")) {
                startClient();
            } else if (btnConn.getText().equals("stop")) {
                stopClient();
            }
        });

        btnSend = new Button("send");
        btnSend.setPrefSize(60, 30);
        btnSend.setDisable(true);
        btnSend.setOnAction(e -> send(txtInput.getText()));

        bottom.setCenter(txtInput);
        bottom.setLeft(btnConn);
        bottom.setRight(btnSend);
        root.setBottom(bottom);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.setOnCloseRequest(event -> stopClient());
        primaryStage.show();
    }


    void displayText(String text) {
        txtDisplay.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}