package tab.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import models.SecretWord;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private final StringProperty nicknameSession = new SimpleStringProperty();
    private SecretWord secretWord;

    // view

    @FXML
    private ListView<String> guessedWordsList;

    @FXML
    private Label hiddenWordLabel;

    @FXML
    private Label livesLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField scoreTextField;

    @FXML
    public TextField wordGuesserField;

    @FXML
    private Label nicknameLabel;

    private MediaPlayer mediaPlayer;

    @FXML
    void onTrySolveAction(ActionEvent event) {

    }

    @FXML
    void onTryWordAction(ActionEvent event) {
        String inputText = wordGuesserField.getText();

        if (inputText != null && !inputText.isEmpty()) {
            char guessedLetter = inputText.charAt(0); // Recoge el primer caracter del textfield

            if (secretWord.guessLetter(guessedLetter) == 0) {
                // Si la letra no está, se añade a las palabras usadas y se resta 1 al score
                scoreTextField.setText(String.valueOf(Integer.parseInt(scoreTextField.getText()) - 1));
                healthPointsUpdate();

            } else {
                // Si la letra se encuentra en la palabra, se actualiza la palabra oculta
                scoreTextField.setText(String.valueOf(Integer.parseInt(scoreTextField.getText()) + 1));
                secretWord.updateHiddenWord();
                hiddenWordLabel.setText(secretWord.getHiddenWord());
            }
            // Añade al palabra usada a la lista
            guessedWordsList.getItems().add(inputText);
            wordGuesserField.clear();
        }
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

    public void startGame() {
        // Start game
        WordsController wordsController = new WordsController();
        Random random = new Random();

        String selectedWord = wordsController.wordsListProperty().get(random.nextInt(wordsController.wordsListProperty().size()));

        // Create the secret word and bind its hidden word to the label
        secretWord = new SecretWord(selectedWord);

        // Bind the hidden word property of secretWord to the hiddenWordLabel text
        hiddenWordLabel.textProperty().bindBidirectional(secretWord.hiddenWordProperty());

        // Reset fields
        livesLabel.setText("\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4");
        guessedWordsList.setEditable(true);
        guessedWordsList.getItems().clear();
        wordGuesserField.clear();
        scoreTextField.setText("0");
    }

    private void healthPointsUpdate() {
        String currentLives = livesLabel.getText(); // Get current text from livesLabel
        if (!currentLives.isEmpty()) {
            String updatedLives = currentLives.substring(0, currentLives.length() - 2); // Remove last character
            livesLabel.setText(updatedLives); // Update livesLabel with new string
        }
        if (currentLives.isEmpty()) {
            Alert gameOverAlert = new Alert(Alert.AlertType.WARNING);
            gameOverAlert.setContentText("Game Over");
            gameOverAlert.showAndWait();
        }
    }


    // getters and setters
    public Label getHiddenWordLabel() {
        return hiddenWordLabel;
    }

    public void setHiddenWordLabel(Label hiddenWordLabel) {
        this.hiddenWordLabel = hiddenWordLabel;
    }

    public ListView<?> getGuessedWordsList() {
        return guessedWordsList;
    }

    public void setGuessedWordsList(ListView<String> guessedWordsList) {
        this.guessedWordsList = guessedWordsList;
    }
}
