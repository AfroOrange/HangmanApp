package windows.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import javafx.scene.web.WebView;

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

    private void showRules() throws IOException {
        FileReader file = new FileReader(Objects.requireNonNull(getClass().getResource("/rules/rules.md")).getFile());
        StringBuilder content = new StringBuilder();
        String line;

        BufferedReader reader = new BufferedReader(file);
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        String html = renderer.render(parser.parse(content.toString()));

        WebView webView = new WebView();
        webView.getEngine().loadContent(html);
        root.setCenter(webView);
    }
}