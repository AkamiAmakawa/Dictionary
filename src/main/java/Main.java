import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DictionaryInterface.fxml"));
        Pane root = loader.load();
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root, 405, 720));
        primaryStage.show();
    }
}