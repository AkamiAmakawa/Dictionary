package Controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window {
    public void createWindow(String name, String path, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(path));
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }

    public void startWindow() {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DictionaryInterface.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Dictionary");
        Scene mainScene = new Scene(root, 405, 720);
        mainScene.getStylesheets().add(getClass().getClassLoader().getResource("Style.css").toExternalForm());
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
