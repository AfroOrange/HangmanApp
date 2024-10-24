package tab.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class WordsController implements Initializable {

    //model
    private final ListProperty<String> wordsList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final String FILE_PATH = "jsonFiles/words.json";

    //view

    @FXML
    private AnchorPane root;

    @FXML
    private ListView<String> wordsListView;

    @FXML
    private TextField wordsTextField;

    // logic

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showWords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Bindings
        wordsListView.itemsProperty().bindBidirectional(wordsList);

    }

    public void saveWordtoJson() {
        Gson gson = new Gson();
        String jsonContent = gson.toJson(wordsList);
        Path jsonFilePath = Paths.get(FILE_PATH);
        try {
            Files.writeString(jsonFilePath, jsonContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public ObservableList<String> getWordsList() {
        return wordsList.get();
    }

    public ListProperty<String> wordsListProperty() {
        return wordsList;
    }

    public ListView<String> getWordsListView() {
        return wordsListView;
    }

    @FXML
    void onAddWordButton(ActionEvent event) {
        if (wordsTextField.getText().isEmpty()) {
            return;
        }

        String word = wordsTextField.getText();
        wordsList.add(word);

        // Save the word in the json file
        saveWordtoJson();

        wordsTextField.clear();
    }

    @FXML
    void onRemoveWordAction(ActionEvent event) {
        String selectedWord = wordsListView.getSelectionModel().getSelectedItem();
        wordsList.remove(selectedWord);

        // Save the word in the json file
        saveWordtoJson();
    }

    private void showWords() throws IOException {
        Gson gson = new Gson();

        Path jsonFilePath = Paths.get(FILE_PATH);

        // Comprueba si el archivo existe
        if (!Files.exists(jsonFilePath)) {
            throw new FileNotFoundException("The file " + FILE_PATH + " was not found");
        }

        String jsonContent = Files.readString(jsonFilePath);
        List<String> palabrasList = gson.fromJson(jsonContent, new TypeToken<List<String>>() {}.getType());

        wordsList.addAll(palabrasList);
    }
}
