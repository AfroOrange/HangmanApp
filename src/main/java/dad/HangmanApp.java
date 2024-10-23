package dad;

import javafx.scene.image.Image;
import windows.controllers.LoginController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HangmanApp extends Application {

    private final LoginController rootController = new LoginController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene hangmanScene = new Scene(rootController.getRoot());

        Stage hangmanStage = new Stage();
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResource("/images/9.png")).toString());

        hangmanStage.getIcons().add(appIcon);
        hangmanStage.setTitle("Hangman Game");
        hangmanStage.setScene(hangmanScene);
        hangmanStage.show();
    }
}
