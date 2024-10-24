package tab.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import models.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private ListView<String> scoreBoardList;

    private List<Users> usersList;

    private final String FILE_PATH = "jsonFiles/users.json";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showScoreBoardData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ScoreBoardController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoreboardControllerView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getRoot() {
        return root;
    }

    private void showScoreBoardData() throws IOException {
        Gson gson = new Gson();

        Path jsonFilePath = Paths.get(FILE_PATH);

        // Comprueba si el archivo existe
        if (!Files.exists(jsonFilePath)) {
            throw new FileNotFoundException("The file " + FILE_PATH + " was not found");
        }

        String jsonContent = Files.readString(jsonFilePath);
        List<Users> usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {}.getType());

        scoreBoardList.getItems().clear();

        for (Users user : usersList) {
            String userDisplay = user.getName() + " | Score: " + user.getScore();
            scoreBoardList.getItems().add(userDisplay);
        }
    }

    public boolean checkUsername(String nickname) throws IOException {
        Path jsonFilePath = Paths.get(FILE_PATH);

        if (!Files.exists(jsonFilePath)) {
            return false;
        }

        String jsonContent = Files.readString(jsonFilePath);
        List<Users> usersList = new Gson().fromJson(jsonContent, new TypeToken<List<Users>>() {}.getType());

        for (Users user : usersList) {
            if (user.getName().equalsIgnoreCase(nickname)) {
                return true;
            }
        }

        return false;
    }

}
