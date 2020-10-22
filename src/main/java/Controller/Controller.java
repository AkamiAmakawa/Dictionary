package Controller;

import MainProgram.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    private static final int maxWord = 20;
    protected static Dictionary engDict = new Dictionary();
    protected static Word currentWord = new Word();
    protected static SQLHandle dataBase;
    protected static boolean Online = false;
    protected static boolean dbLoaded = false;
    private final ToggleGroup displayWord = new ToggleGroup();
    private final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    @FXML
    private VBox wordBox;
    @FXML
    private WebView definitionBox;
    @FXML
    private TextField wordSearcher;
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

    private Task<ArrayList<Word>> createSearchTask() {
        Task<ArrayList<Word>> task = new Task<ArrayList<Word>>() {
            @Override
            protected ArrayList<Word> call() {
                try {
                    return dataBase.dictionarySearcher(wordSearcher.getText(), maxWord);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> displayResult(task.getValue()));
        return task;
    }

    private Task<Void> loadData() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                ResultSet data = dataBase.getData();
                DictionaryManagement.insertFromDB(Controller.engDict, data);
                return null;
            }
        };
        task.setOnSucceeded(event -> dbLoaded = true);
        task.setOnFailed(event -> System.out.println("Failed"));
        return task;
    }

    /**
     * Run dictionarySearcher method everytime a key is release
     */
    private void runWordSearcher() {
        ArrayList<Word> searchResult = new ArrayList<>();
        String target = wordSearcher.getText().replaceAll("\\s+", " ");
        Pattern noSpace = Pattern.compile(" ?([^ ].*?[^ ]) ?");
        Matcher noSpaceMatch = noSpace.matcher(target);
        if (noSpaceMatch.matches()) {
            target = noSpaceMatch.group(0);
        }
        if (dbLoaded || !Online) {
            searchResult = DictionaryManagement.dictionarySearcher(engDict, target, maxWord);
            displayResult(searchResult);
        } else {
            executorService.getQueue().clear();
            executorService.execute(createSearchTask());
        }
    }

    private void displayResult(ArrayList<Word> searchResult) {
        //clear the box for new display
        wordBox.getChildren().clear();
        definitionBox.getEngine().loadContent("");
        //selected is for getting the first element selected by default
        boolean selected = true;
        if (searchResult.size() <= 0 && (!Online || dbLoaded)) {
            searchResult = DictionaryManagement.suggestWord(engDict, wordSearcher.getText(), maxWord);
        }
        for (Word word : searchResult) {
            RadioButton temp = createWord(word);
            //select the first word
            if (selected) {
                temp.setSelected(true);
                showDefinition(word);
                selected = false;
                currentWord = word;
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
        wordSelect.setOnMouseClicked(a -> showDefinition(word));
        wordSelect.setToggleGroup(displayWord);
        wordSelect.getStyleClass().add("wordButton");
        return wordSelect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Online) {
            Thread loadData = new Thread(loadData());
            loadData.setDaemon(true);
            loadData.start();
        }
        runWordSearcher();
        {
            Image image = new Image(getClass().getClassLoader().getResource("Speaker.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.fitHeightProperty();
            imageView.fitWidthProperty();
            imageView.setFitHeight(22);
            imageView.setFitWidth(22);
            btSpeak.setPrefSize(8, 8);
            btSpeak.setGraphic(imageView);
        }

        btSpeak.setOnMouseClicked(event -> {
            ApiCommandLine.Speak(currentWord.getWord_target());
        });

        MenuItem exitItem = new MenuItem("Exit");

        // Sét đặt phím tắt cho MenuItem Exit.
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));

        // Thiết lập sự kiện khi người dùng chọn vào Exit.
        exitItem.setOnAction(event -> System.exit(0));

        fileMenu.getItems().addAll(exitItem);

        addItem.setOnAction(event -> {
            String s = "AddWindow.fxml";
            Window window = new Window();
            try {
                window.createWindow("Add Word", s, 654, 547);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteItem.setOnAction(event -> {
            Window message = new Window();
            Word target = currentWord;
            MiscellaneousController.buttons.clear();
            MiscellaneousController.msg = "Are you sure to remove the word " + target.getWord_target();
            Button confirm = new Button();
            confirm.setText("Yes");
            confirm.setOnAction(e -> {
                if (Online) {
                    dataBase.deleteWord(target.getDbID());
                }
                engDict.getGroup(Character.toUpperCase(target.getWord_target().charAt(0))).remove(target);
                DictionaryManagement.dictionaryUpdate(engDict);
                Stage sToClose = (Stage) confirm.getScene().getWindow();
                sToClose.close();
                runWordSearcher();
            });
            Button cancel = new Button();
            cancel.setText("No");
            cancel.setOnAction(e -> {
                Stage sToClose = (Stage) cancel.getScene().getWindow();
                sToClose.close();
            });
            MiscellaneousController.buttons.add(confirm);
            MiscellaneousController.buttons.add(cancel);
            try {
                message.createWindow("Message", "ConfirmWindow.fxml", 625, 215);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        EditItem.setOnAction(event -> {
            String s = "ChangeWindow.fxml";
            Window window = new Window();
            try {
                window.createWindow("Word Edit", s, 654, 547);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sentence.setOnAction(event -> {
            String s = "SentenceWindow.fxml";
            Window window = new Window();
            try {
                window.createWindow("Online Translator", s, 654, 547);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

