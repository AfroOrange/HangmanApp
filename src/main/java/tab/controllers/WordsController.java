package tab.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WordsController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextArea wordsTextArea;

    @FXML
    void onAddWordButton(ActionEvent event) {

    }

    @FXML
    void onRemoveWordAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public WordsController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WordsControllerView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getRoot() {
        return root;
    }
}
