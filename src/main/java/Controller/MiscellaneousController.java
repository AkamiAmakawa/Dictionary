package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MiscellaneousController implements Initializable {
    protected static ArrayList<Button> buttons = new ArrayList<>();
    protected static String msg = "";
    @FXML
    private Label message;
    @FXML
    private HBox buttonArray;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        message.setText(msg);
        for (int i = 0; i < buttons.size(); i++) {
            buttonArray.getChildren().add(buttons.get(i));
        }
    }
}
