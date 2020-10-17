import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    private static final int maxWord = 20;
    private final ToggleGroup displayWord = new ToggleGroup();
    Dictionary engDict = new Dictionary();
    private Word currentWord = new Word();
    @FXML
    private VBox wordBox;
    @FXML
    private WebView definitionBox;
    @FXML
    private TextField wordSearcher;
    private SQLHandle dataBase;

    @FXML
    private MenuBar mbMenu;

    @FXML
    private Menu fileMenu;


    @FXML
    private Button btSpeak;

    @FXML
    private MenuItem addItem;

    @FXML
    private MenuItem deleteItem;

    @FXML
    private MenuItem EditItem;

    @FXML
    private MenuItem sentence;

    @FXML
    private void startWordSearcher() {
        runWordSearcher();
    }

    /**
     * Run dictionarySearcher method everytime a key is release
     */
    private void runWordSearcher() {
        //clear the box for new display
        wordBox.getChildren().clear();
        definitionBox.getEngine().loadContent("");
        //selected is for getting the first element selected by default
        boolean selected = true;
        String target = wordSearcher.getText().replaceAll("\\s+", " ");
        Pattern noSpace = Pattern.compile(" ?([^ ].*?[^ ]) ?");
        Matcher noSpaceMatch = noSpace.matcher(target);
        if (noSpaceMatch.matches()) {
            target = noSpaceMatch.group(0);
        }
        ArrayList<Word> searchResult
                = DictionaryManagement.dictionarySearcher(engDict, target, maxWord);
        //          = dataBase.dictionarySearcher(wordSearcher.getText(), maxWord);
        if (searchResult.size() <= 0) {
            searchResult = DictionaryManagement.suggestWord(engDict, wordSearcher.getText(), maxWord);
        }
        for (int i = 0; i < searchResult.size(); i++) {
            RadioButton temp = createWord(searchResult.get(i));
            //select the first word
            if (selected) {
                temp.setSelected(true);
                showDefinition(searchResult.get(i));
                selected = false;
            }
            wordBox.getChildren().add(temp);
        }
    }

    private void showDefinition(Word word) {
        this.definitionBox.getEngine().loadContent(word.getWord_explain());
        currentWord = word;
    }

    /**
     * Initialize a radio button to display and choose word
     */
    private RadioButton createWord(Word word) {
        RadioButton wordSelect = new RadioButton();
        wordSelect.getStyleClass().remove("radio-button");
        wordSelect.setPrefWidth(wordBox.getPrefWidth());
        wordSelect.setText(word.getWord_target());
        wordSelect.setOnAction(a -> showDefinition(word));
        wordSelect.setToggleGroup(displayWord);
        return wordSelect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DictionaryManagement.insertFromFile(engDict);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

//
//        try {
//            dataBase = new SQLHandle();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        runWordSearcher();
        Button Speak;
        {
            Image image = null;
            try (FileInputStream input = new FileInputStream("Speaker.png")) {
                image = new Image(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.fitHeightProperty();
            imageView.fitWidthProperty();
            imageView.setFitHeight(22);
            imageView.setFitWidth(22);
            btSpeak.setPrefSize(8, 8);
            btSpeak.setGraphic(imageView);
        }

        btSpeak.setOnMouseClicked(event -> {
            ApiCommandLine speak = new ApiCommandLine();
            speak.Speak(currentWord.getWord_target());
        });

        MenuItem exitItem = new MenuItem("Exit");

        // Sét đặt phím tắt cho MenuItem Exit.
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));

        // Thiết lập sự kiện khi người dùng chọn vào Exit.
        exitItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        fileMenu.getItems().addAll(exitItem);

        addItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage addWindow = new Stage();
                String s = "AddWindow.fxml";
                Window window = new Window();
                try {
                    window.Window(addWindow,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage deleteWindow = new Stage();
                String s = "DeleteWindow.fxml";
                Window window = new Window();
                try {
                    window.Window(deleteWindow,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        EditItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage changeWindow = new Stage();
                String s = "ChangeWindow.fxml";
                Window window = new Window();
                try {
                    window.Window(changeWindow,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sentence.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage translate = new Stage();
                String s = "SentenceWindow.fxml";
                Window window = new Window();
                try {
                    window.Window(translate,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
