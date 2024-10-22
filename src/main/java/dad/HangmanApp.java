package dad;

import javafx.scene.image.Image;
import windows.controllers.LoginController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanApp extends Application {

    private Image appIcon;

    private final LoginController rootController = new LoginController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene hangmanScene = new Scene(rootController.getRoot());

        Stage hangmanStage = new Stage();
        appIcon = new Image(getClass().getResource("/images/9.png").toString());

        hangmanStage.getIcons().add(appIcon);
        hangmanStage.setTitle("Hangman Game");
        hangmanStage.setScene(hangmanScene);
        hangmanStage.show();
    }
}
