package Controller;

import MainProgram.DictionaryManagement;
import MainProgram.SQLHandle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class StartController {
    protected static Boolean Online = false;
    @FXML
    private ImageView indLoc;
    @FXML
    private Button localButton;
    @FXML
    private Button onlineButton;
    @FXML
    private Text msg;

    @FXML
    private void loadOnline() {
        Online = true;
        loadData();
    }

    @FXML
    private void loadLocal() {
        Online = false;
        loadData();

    }

    private Task createTask() {
        Task task = new Task() {
            @Override
            protected Void call() {
                localButton.setDisable(true);
                onlineButton.setDisable(true);
                indLoc.setImage(new Image(
                        getClass().getClassLoader().getResource("data/loading.gif").toExternalForm()));
                if (!Online) {
                    try {
                        DictionaryManagement.insertFromFile(Controller.engDict);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                if (Online) {
                    try {
                        Controller.dataBase = new SQLHandle();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Controller.Online = true;
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            Window startWindow = new Window();
            startWindow.startWindow();
            Stage stage = (Stage) localButton.getScene().getWindow();
            stage.close();
        });
        task.setOnFailed(event -> {
            indLoc.setImage(null);
            localButton.setDisable(false);
            onlineButton.setDisable(false);
            msg.setText("Load error please try again");
        });
        return task;
    }

    private void loadData() {
        Thread thread = new Thread(createTask());
        thread.setDaemon(true);
        thread.start();
    }

}
