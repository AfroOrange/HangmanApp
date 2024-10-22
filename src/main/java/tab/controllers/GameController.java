package tab.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import windows.controllers.LoginController;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private final StringProperty nicknameSession = new SimpleStringProperty();

    @FXML
    private AnchorPane root;

    @FXML
    private TextArea scoreBox;

    @FXML
    private TextField wordGuesserField;

    @FXML
    private Label nicknameLabel;

    @FXML
    void onTrySolveAction(ActionEvent event) {

    }

    @FXML
    void onTryWordAction(ActionEvent event) {

    }

    public GameController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameControllerView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nicknameLabel.textProperty().bind(nicknameSession);
    }

    public AnchorPane getRoot() {
        return root;
    }

    public void setNickname(String nickname) {
        nicknameSession.set(nickname);
    }
}
