package dad;

import windows.controllers.LoginController;
import windows.controllers.RootController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanApp extends Application {

    private final LoginController rootController = new LoginController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene hangmanScene = new Scene(rootController.getRoot());

        Stage hangmanStage = new Stage();
        hangmanStage.setTitle("Hangman Game");
        hangmanStage.setScene(hangmanScene);
        hangmanStage.show();
    }
}
