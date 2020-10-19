package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    protected static Boolean Online = false;
    @FXML
    private ImageView indLoc;

    @FXML
    private void loadOnline() {
        Online = true;
        load();
    }

    @FXML
    private void loadLocal() {
        Online = false;
        load();

    }

    private void load() {
        indLoc.setImage(new Image(getClass().getClassLoader().getResource("data/loading.gif").toExternalForm()));
        Window startWindow = new Window();
        startWindow.startWindow();
        Stage sClose = (Stage) indLoc.getScene().getWindow();
        sClose.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
