import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartingScreen.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Dictionary");
        Scene mainScene = new Scene(root, 600, 400);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}