package Controller;

import MainProgram.ApiCommandLine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SentenceController implements Initializable {
    @FXML
    private TextField inputSentence;
    @FXML
    private TextArea meaningBox;
    @FXML
    private Button btSpeak;

    @FXML
    private void runSentenceTranslate() {
        String result = ApiCommandLine.onlineTranslator(inputSentence.getText());
        meaningBox.setText(result);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(getClass().getClassLoader().getResource("Speaker.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty();
        imageView.fitWidthProperty();
        imageView.setFitHeight(22);
        imageView.setFitWidth(22);
        btSpeak.setPrefSize(8, 8);
        btSpeak.setGraphic(imageView);

        btSpeak.setOnMouseClicked(event -> {
            ApiCommandLine.Speak(inputSentence.getText());
        });
    }
}
