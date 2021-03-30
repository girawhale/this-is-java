package chap19;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class WatchServiceExample extends Application {
    class WatchServiceThread extends Thread {
        @Override
        public void run() {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path directory = Paths.get("src/chap19/dir");
                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey watchKey = watchService.take(); // 블로킹(WatchKey가 큐에 들어올 때까지)
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();

                    for (WatchEvent watchEvent : watchEvents) {
                        WatchEvent.Kind kind = watchEvent.kind(); //이벤트 종류 얻기
                        Path path = (Path) watchEvent.context(); //감지된 Path 열기

                        // 이벤트 종류별 처리
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            // 생성 실행 코드
                            Platform.runLater(() -> textArea.appendText("파일 생성 ->" + path.getFileName() + "\n"));
                        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            // 삭제 실행 코드
                            Platform.runLater(() -> textArea.appendText("파일 삭제 ->" + path.getFileName() + "\n"));
                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            // 변경 실행 코드
                            Platform.runLater(() -> textArea.appendText("파일 변경 ->" + path.getFileName() + "\n"));
                        } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                        }
                    }
                    if (!watchKey.reset()) break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    TextArea textArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);
        textArea = new TextArea();
        textArea.setEditable(false);
        root.setCenter(textArea);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WatchServiceExample");
        primaryStage.show();

        WatchServiceThread wst = new WatchServiceThread();
        wst.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
