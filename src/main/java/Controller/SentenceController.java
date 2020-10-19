package Controller;

import MainProgram.ApiCommandLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SentenceController {
    @FXML
    private TextField inputSentence;
    @FXML
    private TextArea meaningBox;

    @FXML
    private void runSentenceTranslate() {
        String result = ApiCommandLine.onlineTranslator(inputSentence.getText());
        meaningBox.setText(result);
    }
}
