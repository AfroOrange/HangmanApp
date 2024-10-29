package windows.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import tab.controllers.GameController;
import tab.controllers.ScoreBoardController;
import tab.controllers.WordsController;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RootController implements Initializable {

        // controllers
        private final WordsController wordsController = new WordsController();
        private final Tab wordsTab = new Tab("Words");

        private MediaPlayer backgroundMusic;
        private final GameController gameController;

        // view

        @FXML
        private Slider volumeSlider;

        @FXML
        private TabPane editionTabPane;

        @FXML
        private BorderPane root;

        public RootController(GameController gameController) {
            this.gameController = gameController;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootControllerView.fxml"));
                loader.setController(this);
                loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // borra las pestañas default
        editionTabPane.getTabs().clear();

        Tab gameTab = new Tab("Game");
        gameTab.setContent(gameController.getRoot());

        wordsTab.setContent(wordsController.getRoot());

        Tab scoreBoardTab = new Tab("Score Board");
        scoreBoardTab.setContent(new ScoreBoardController().getRoot());

        editionTabPane.getTabs().addAll(gameTab, wordsTab, scoreBoardTab);

        gameController.wordGuesserField.setEditable(false);
    }

    @FXML
    public void onStopGameAction() {
        // add an alert and reveal the hidden word
        Alert stopGameAlert = new Alert(Alert.AlertType.INFORMATION);
        stopGameAlert.setTitle("Game Finished");
        stopGameAlert.setHeaderText(null);
        stopGameAlert.setContentText("Try Again!");
        stopGameAlert.showAndWait();

        gameController.getHiddenWordLabel().setText("PRESS F2 TO START A NEW GAME!");

        // clear fields
        gameController.getGuessedWordsList().getItems().clear();
        gameController.wordGuesserField.clear();
        gameController.getLivesLabel().setText(" ");
        gameController.getScoreTextField().setText(" ");

        //disable buttons
        gameController.wordGuesserField.setEditable(false);
        gameController.getTrySolveButton().setDisable(true);
        gameController.getTryWordButton().setDisable(true);

        // se para la música
        backgroundMusic.stop();

        // habilita de nuevo wordTab
        wordsTab.setDisable(false);
        }

    @FXML
    void onNewGameAction(ActionEvent event) {
        gameController.wordGuesserField.setEditable(true);
        gameController.getHiddenWordLabel().setText("");
        gameController.startGame();
        wordsTab.setDisable(true);

        // background music starts playing
        playInGameBackgroundMusic();

        // bind volume slider to background music volume
        volumeSlider.setValue(backgroundMusic.getVolume());
        backgroundMusic.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
    }

    @FXML
    void onHelpAction(ActionEvent event) {
        HelpController helpController = new HelpController();

        Scene helpScene = new Scene(helpController.getRoot());
        Stage helpStage = new Stage();
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResource("/images/question_mark.png")).toString());

        helpStage.getIcons().add(appIcon);
        helpStage.setTitle("Hangman Game");
        helpStage.setScene(helpScene);
        helpStage.show();
    }

    private void playInGameBackgroundMusic() {
        String musicPath = Objects.requireNonNull(getClass().getResource("/music/ingame_background_music_oria.wav")).toExternalForm();

        backgroundMusic = new MediaPlayer(new Media(musicPath));

        backgroundMusic.setVolume(0.1);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);

        backgroundMusic.play();
    }

    public BorderPane getRoot() {
        return root;
    }
}
