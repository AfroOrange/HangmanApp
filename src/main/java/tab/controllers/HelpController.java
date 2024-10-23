package tab.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TextArea informationArea;

    @FXML
    void onCloseAction(ActionEvent e) {
        root.getScene().getWindow().hide();
    }
    public BorderPane getRoot() {
        return root;
    }

    public HelpController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HelpControllerView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showRules();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showRules() throws IOException {
        FileReader file = new FileReader("rules/rules.txt");
        String line;
        BufferedReader reader = new BufferedReader(file);
        while ((line = reader.readLine()) != null) {
            informationArea.appendText(line + "\n");
        }
    }
}