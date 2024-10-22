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

    private void showScoreBoardData() throws IOException, FileNotFoundException {
        Gson gson = new Gson();

        // Define the path to the JSON file (adjust the path as per your directory structure)
        Path jsonFilePath = Paths.get("users/users.json");

        // Check if the file exists
        if (!Files.exists(jsonFilePath)) {
            throw new FileNotFoundException("The file 'users/users.json' was not found.");
        }

        String jsonContent = Files.readString(jsonFilePath);

        // Parse the JSON into a list of Users
        List<Users> usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {}.getType());

        // Clear the ListView before adding items
        scoreBoardList.getItems().clear();

        // Populate the ListView with formatted user names and scores
        for (Users user : usersList) {
            String userDisplay = user.getName() + " - " + user.getScore();
            scoreBoardList.getItems().add(userDisplay); // Add each user display string to the ListView
        }
    }

}
