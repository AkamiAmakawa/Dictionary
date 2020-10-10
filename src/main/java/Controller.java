import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final int maxWord = 20;
    private final ToggleGroup displayWord = new ToggleGroup();
    Dictionary engDict = new Dictionary();
    @FXML
    private VBox wordBox;
    @FXML
    private WebView definitionBox;
    @FXML
    private TextField wordSearcher;
    private SQLHandle dataBase;

    @FXML
    /**
     * Run dictionarySearcher method everytime a key is release
     */
    private void runWordSearcher() {
        //clear the box for new display
        wordBox.getChildren().clear();
        definitionBox.getEngine().loadContent("");
        //selected is for getting the first element selected by default
        boolean selected = true;
        ArrayList<Word> searchResult
                = dataBase.dictionarySearcher(wordSearcher.getText(), maxWord);
        for (int i = 0; i < searchResult.size(); i++) {
            RadioButton temp = createWord(searchResult.get(i));
            //select the first word
            if (selected) {
                temp.setSelected(true);
                showDefinition(searchResult.get(i).getWord_explain());
                selected = false;
            }
            wordBox.getChildren().add(temp);
        }
    }

    private void showDefinition(String definition) {
        this.definitionBox.getEngine().loadContent(definition);
    }

    /**
     * Initialize a radio button to display and choose word
     */
    private RadioButton createWord(Word word) {
        RadioButton wordSelect = new RadioButton();
        wordSelect.getStyleClass().remove("radio-button");
        wordSelect.getStyleClass().add("toggle-button");
        wordSelect.setPrefWidth(wordBox.getPrefWidth());
        wordSelect.setText(word.getWord_target());
        wordSelect.setOnAction(a -> showDefinition(word.getWord_explain()));
        wordSelect.setToggleGroup(displayWord);
        return wordSelect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*        try {
            DictionaryManagement.insertFromFile(engDict);
            runWordSearcher();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

 */
        try {
            dataBase = new SQLHandle();
            runWordSearcher();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
