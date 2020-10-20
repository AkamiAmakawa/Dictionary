package Controller;

import MainProgram.DictionaryManagement;
import MainProgram.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Controller.Controller.*;

public class AddController implements Initializable {

    @FXML
    private TextField wordField;
    @FXML
    private HTMLEditor definitionBox;

    @FXML
    private void addWord() {
        Window message = new Window();
        String target = wordField.getText();
        if (target.equals("")) {
            MiscellaneousController.msg = "Word field can not be empty";
            Button temp = new Button();
            temp.setOnAction(e -> {
                Stage stageToClose = (Stage) temp.getScene().getWindow();
                stageToClose.close();
            });
            temp.setText("Continues");
            MiscellaneousController.buttons.clear();
            MiscellaneousController.buttons.add(temp);
            try {
                message.createWindow("Error", "ConfirmWindow.fxml", 625, 215);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Word temp = new Word();
            temp.setWord_target(wordField.getText());
            temp.setWord_explain(definitionBox.getHtmlText());
            if (Online) {
                dataBase.addWord(temp);
                temp.setDbID(dataBase.getCurrentID());
            }
            engDict.addWord(temp);
            DictionaryManagement.dictionaryUpdate(engDict);
            Stage currentStage = (Stage) wordField.getScene().getWindow();
            currentStage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
