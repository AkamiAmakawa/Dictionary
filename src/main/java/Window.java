import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window {
    public void Window  (Stage stage, String name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(name));
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 654, 547));
        stage.show();
    }
}
