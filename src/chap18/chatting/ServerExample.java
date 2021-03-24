package chap18.chatting;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExample extends Application {
    ExecutorService executorService; // 스레드풀
    ServerSocket serverSocket; // 클라이언트 연결 수락
    List<Client> connections = new Vector<>(); // 연결된 클라이언트 저장, 스레드에 안전한 Vector로 초기화

    void startServer() { // 서버 시작 코드
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()); // CPU의 코어수만큼 스레드 생성

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));

            Runnable runnable = () -> {
                Platform.runLater(() -> {
                    displayText("[서버 시작]");
                    btnStartStop.setText("STOP");
                });

                while (true) {
                    try {
                        Socket socket = serverSocket.accept(); // 연결 수락
                        String msg = "[연결 수락 : " + socket.getRemoteSocketAddress() +
                                " : " + Thread.currentThread().getName() + "]";
                        Platform.runLater(() -> displayText(msg));

                        Client client = new Client(socket); // 객체 저장
                        connections.add(client);
                        Platform.runLater(() -> displayText("[연결 개수 : " + connections.size() + "]"));

                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) stopServer();
                        break;
                    }
                }
            };

            executorService.submit(runnable); // 스레드풀에서 처리

        } catch (IOException e) {
            if (!serverSocket.isClosed()) stopServer();
            return;
        }
    }


    void stopServer() { // 서버 종료 코드
        try {
            Iterator<Client> iterator = connections.iterator();

            while (iterator.hasNext()) {// 모든 소켓 닫기
                Client client = iterator.next();
                client.socket.close();
                iterator.remove();
            }

            if (serverSocket != null && !serverSocket.isClosed()) // 서버 소켓 닫기
                serverSocket.close();

            if (executorService != null && !executorService.isShutdown()) // 스레드풀 닫기
                executorService.shutdown();

            Platform.runLater(() -> {
                displayText("[서버 멈춤]");
                btnStartStop.setText("START");
            });

        } catch (Exception e) {
        }
    }


    class Client { // 데이터 통신 코드
        Socket socket;

        public Client(Socket socket) {
            this.socket = socket;
            receive();
        }

        void receive() { // 데이터 받기 코드
            Runnable runnable = () -> {
                try {
                    while (true) {
                        byte[] bytes = new byte[100];
                        InputStream is = socket.getInputStream();

                        //클라이언트가 비정상 종료시 IOException 발생
                        int readByteCnt = is.read(bytes);

                        // 클라이언트가 정상적으로 Socket의 close()를 호출했을 때
                        if (readByteCnt == -1) throw new IOException();

                        String msg = "[요청 처리 : " + socket.getRemoteSocketAddress() +
                                " : " + Thread.currentThread().getName() + "]";

                        Platform.runLater(() -> displayText(msg));

                        String data = new String(bytes, 0, readByteCnt, StandardCharsets.UTF_8);
                        for (Client client : connections)
                            client.send(data);

                    }
                } catch (IOException e) {
                    try {
                        connections.remove(Client.this);
                        String msg = "[클라이언트 통신 불가 : " + socket.getRemoteSocketAddress() +
                                " : " + Thread.currentThread().getName() + "]";
                        Platform.runLater(() -> displayText(msg));
                        socket.close();

                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }

                }
            };

            executorService.submit(runnable);
        }

        void send(String data) { // 데이터 전송 코드
            Runnable runnable = () -> {
                try {
                    // 클라이언트로 데이터 보내기
                    byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                    OutputStream os = socket.getOutputStream();
                    os.write(bytes);
                    os.flush();

                } catch (IOException e) {
                    try {
                        connections.remove(Client.this);
                        String msg = "[클라이언트 통신 불가 : " + socket.getRemoteSocketAddress() +
                                " : " + Thread.currentThread().getName() + "]";
                        Platform.runLater(() -> displayText(msg));
                        socket.close();

                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }

                }
            };

            executorService.submit(runnable);
        }
    }


    //////////////////////////////////////////////////////////////////////////
    // UI 생성 코드
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

        btnStartStop = new Button("START");
        btnStartStop.setPrefHeight(30);
        btnStartStop.setMaxWidth(Double.MAX_VALUE);
        btnStartStop.setOnAction(e -> {
            if (btnStartStop.getText().equals("START")) {
                startServer();
            } else if (btnStartStop.getText().equals("STOP")) {
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
