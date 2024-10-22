package windows.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import tab.controllers.GameController;
import tab.controllers.ScoreBoardController;
import tab.controllers.WordsController;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

        @FXML
        private TabPane editionTabPane;

        @FXML
        private BorderPane root;

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

    }

    public BorderPane getRoot() {
        return root;
    }
}
