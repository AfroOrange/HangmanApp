package tab.controllers;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import models.ImagesContainer;
import models.SecretWord;
import windows.controllers.RootController;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    // model

    private final StringProperty nicknameSession = new SimpleStringProperty();
    private SecretWord secretWord;
    private ImagesContainer imagesContainer;
    private int imageIndex = 1;

    // view

    @FXML
    private ListView<String> guessedWordsList;

    @FXML
    private Label hiddenWordLabel;

    @FXML
    private Label livesLabel;

    @FXML
    private HBox guessingHbox;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView hangedImage;

    @FXML
    private TextField scoreTextField;

    @FXML
    public TextField wordGuesserField;

    @FXML
    private Label nicknameLabel;

    @FXML
    private Button trySolveButton;

    @FXML
    private Button tryWordButton;

    @FXML
    void onTrySolveAction(ActionEvent event) {
        String guessedWord = wordGuesserField.getText();
        if (Objects.equals(guessedWord, String.valueOf(secretWord.getWord()))) {

            hiddenWordLabel.setText(secretWord.getWord());

            getWinScore();
            gameFinished();
        } else {
            guessedWordsList.getItems().add(guessedWord);
            scoreTextField.setText(String.valueOf(Integer.parseInt(scoreTextField.getText()) - 1));
            healthPointsUpdate();
            imageIndex +=1;
            hangedImage.setImage(imagesContainer.getImage(imageIndex));

            if (imageIndex == 9) {
                int loseScore = Integer.parseInt(scoreTextField.getText()) - 50;
                scoreTextField.setText(String.valueOf(loseScore));

                gameOverAlert();
                gameFinished();
            }
        }
    }

    @FXML
    void onTryWordAction(ActionEvent event) {
        String inputText = wordGuesserField.getText();
        imagesContainer = new ImagesContainer();
        char guessedLetter = inputText.charAt(0); // Recoge el primer caracter del textfield

        if (guessedWordsList.getItems().contains(inputText) && !inputText.equals(" ")) {
            onRepeatedLetter(guessedLetter);
        } else if (!guessedWordsList.getItems().contains(inputText) && !inputText.equals(" ")) {
               if (secretWord.guessLetter(guessedLetter) == 0) {
                   // Si la letra no está, se añade a las palabras usadas y se resta 1 al score
                   scoreTextField.setText(String.valueOf(Integer.parseInt(scoreTextField.getText()) - 1));
                   healthPointsUpdate();

                   imageIndex += 1;
                   if (imageIndex <= 9) {
                       hangedImage.setImage(imagesContainer.getImage(imageIndex));
                   }
                   if (imageIndex == 9) {
                       int loseScore = Integer.parseInt(scoreTextField.getText()) - 50;
                       scoreTextField.setText(String.valueOf(loseScore));

                       gameOverAlert();
                   }
               }
               else {
                   // Si la letra se encuentra en la palabra, se actualiza la palabra oculta
                   scoreTextField.setText(String.valueOf(Integer.parseInt(scoreTextField.getText()) + 1));
                   secretWord.updateHiddenWord();
                   hiddenWordLabel.setText(secretWord.getHiddenWord());

                   // if the player has guessed all the letters, the game is won
                   if (!hiddenWordLabel.getText().contains("_")) {
                         getWinScore();
                         gameFinished();
                   }
               }
               // Add the guessed word to the list
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
        trySolveButton.setDisable(true);
        tryWordButton.setDisable(true);
    }

    public void startGame() {
        // Start game
        WordsController wordsController = new WordsController();
        Random random = new Random();

        // enable buttons
        tryWordButton.setDisable(false);
        trySolveButton.setDisable(false);

        String selectedWord = wordsController.wordsListProperty().get(random.nextInt(wordsController.wordsListProperty().size()));

        // Create the secret word and bind its hidden word to the label
        secretWord = new SecretWord(selectedWord);
        // Bind the hidden word property of secretWord to the hiddenWordLabel text
        hiddenWordLabel.textProperty().bindBidirectional(secretWord.hiddenWordProperty());

        // Reset fields
        resetFields();
    }

    // alert to inform the player they've lost the game
    private void gameOverAlert() {
        Alert gameOverAlert = new Alert(Alert.AlertType.WARNING);
        gameOverAlert.setTitle("Game Over :(");
        gameOverAlert.setContentText(String.format("The secret word was: %s", secretWord.getWord()));
        gameOverAlert.showAndWait();

        // add music on defeat
        setGameOverSound();

        // game ends and disable buttons
        gameFinished();
    }

    private void onRepeatedLetter(char inputText) {
        Label wrongLetterLabel = new Label(String.format("Letter '%s' already introduced", inputText));
        wrongLetterLabel.setStyle("-fx-background-color: orange; -fx-padding: 10;");

        // añadir coordenadas del hbox para centrar la etiqueta
        wrongLetterLabel.setLayoutX(guessingHbox.getLayoutX() + 75);
        wrongLetterLabel.setLayoutY(guessingHbox.getLayoutY() + guessingHbox.getHeight() - 17);
        root.getChildren().add(wrongLetterLabel);

        // Animación para desvanecer el mensaje
        FadeTransition fade = new FadeTransition(Duration.seconds(2), wrongLetterLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> root.getChildren().remove(wrongLetterLabel));
        fade.play();
    }

    private void gameFinished() {
        hiddenWordLabel.setText("PRESS F3 FINISH THE GAME!");
        wordGuesserField.clear();
        wordGuesserField.setEditable(false);
        tryWordButton.setDisable(true);
        trySolveButton.setDisable(true);
    }

    private void resetFields() {
        // condition to check if imagesContainer is null
        if (imagesContainer == null) {
            imagesContainer = new ImagesContainer();
        }
        // Reset image and update imageIndex
        imageIndex = 1;
        hangedImage.setImage(imagesContainer.getImage(imageIndex));

        // Reset fields
        livesLabel.setText("\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4\uD83D\uDDA4");
        guessedWordsList.setEditable(true);
        guessedWordsList.getItems().clear();
        wordGuesserField.clear();
        scoreTextField.setText("0");
    }

    private void setGameOverSound() {
        String musicPath = Objects.requireNonNull(getClass().getResource("/music/socorro.mp3")).toExternalForm();
        MediaPlayer gameOverSound = new MediaPlayer(new Media(musicPath));

        gameOverSound.setVolume(0.5);
        gameOverSound.play();
    }

    private void healthPointsUpdate() {
        String currentLives = livesLabel.getText(); // Get current text from livesLabel
        if (!currentLives.isEmpty()) {
            String updatedLives = currentLives.substring(0, currentLives.length() - 2); // Remove last character
            livesLabel.setText(updatedLives); // Update livesLabel with new string
        }
    }

    private void getWinScore () {
        int livesScore = livesLabel.getText().length() / 2;
        int wordScore = Integer.parseInt(scoreTextField.getText());
        int winScore = (livesScore * wordScore) + 50;

        scoreTextField.setText(String.valueOf((winScore)));

        ScoreBoardController scoreBoardController = new ScoreBoardController();

        try {
            scoreBoardController.saveScoreToJson(nicknameSession.get(), winScore);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // message to inform the final score to the player
        Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
        winAlert.setTitle("Game Won!");
        winAlert.setContentText(String.format("Contrats, you got %s points", scoreTextField.getText()));
        winAlert.showAndWait();
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

    public TextField getScoreTextField() {
        return scoreTextField;
    }

    public void setScoreTextField(TextField scoreTextField) {
        this.scoreTextField = scoreTextField;
    }

    public SecretWord getSecretWord() {
        return secretWord;
    }

    public Label getLivesLabel() {
        return livesLabel;
    }

    public HBox getGuessingHbox() {
        return guessingHbox;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public void setNickname(String nickname) {
        nicknameSession.set(nickname);
    }

    public Button getTrySolveButton() {
        return trySolveButton;
    }

    public Button getTryWordButton() {
        return tryWordButton;
    }
}
