package windows.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import tab.controllers.GameController;
import tab.controllers.HelpController;
import tab.controllers.ScoreBoardController;
import tab.controllers.WordsController;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RootController implements Initializable {

        @FXML
        private Slider volumeSlider;

        @FXML
        private TabPane editionTabPane;

        @FXML
        private BorderPane root;

        private MediaPlayer backgroundMusic;

        private final GameController gameController;

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

        Tab wordsTab = new Tab("Words");
        wordsTab.setContent(new WordsController().getRoot());

        Tab scoreBoardTab = new Tab("Score Board");
        scoreBoardTab.setContent(new ScoreBoardController().getRoot());

        editionTabPane.getTabs().addAll(gameTab, wordsTab, scoreBoardTab);

        // Se inicia la música de fondo
        playInGameBackgroundMusic();

        // bindear el volumen de la música al slider
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
