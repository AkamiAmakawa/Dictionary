package Controller;

import MainProgram.DictionaryManagement;
import MainProgram.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static Controller.Controller.*;

public class ChangeController implements Initializable {
    @FXML
    private HTMLEditor definitionEditBox;
    @FXML
    private TextField wordToEdit;
    private final Word target = currentWord;

    @FXML
    private void runWordChange() {
        String meaning = definitionEditBox.getHtmlText();
        target.setWord_explain(meaning);
        if (Online) {
            Controller.dataBase.changeWord(target.getWord_target(), meaning);
        }
        DictionaryManagement.dictionaryUpdate(engDict);
        Stage sClose = (Stage) wordToEdit.getScene().getWindow();
        sClose.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordToEdit.setText(target.getWord_target());
        definitionEditBox.setHtmlText(target.getWord_explain());
    }
}
