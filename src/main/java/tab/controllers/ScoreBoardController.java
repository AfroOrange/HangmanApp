package tab.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable {

    // model
    private List<Users> usersList;
    private final String FILE_PATH = "jsonFiles/users.json";

    // view
    @FXML
    private TableColumn<Users, String> nicknameColum;

    @FXML
    private TableView<Users> playerScoreboardTable;

    @FXML
    private AnchorPane root;

    @FXML
    private TableColumn<Users, Integer> scoreColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nicknameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

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

    // logic
    private void showScoreBoardData() throws IOException {
        Gson gson = new Gson();

        Path jsonFilePath = Paths.get(FILE_PATH);

        if (!Files.exists(jsonFilePath)) {
            throw new FileNotFoundException("The file " + FILE_PATH + " was not found");
        }

        // Check if the file exists, and read its content if it does
        if (Files.exists(jsonFilePath)) {
            String jsonContent = Files.readString(jsonFilePath);

            // Deserialize the existing users from JSON
            usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {}.getType());

            // Handle the case where JSON content is malformed or empty
            if (usersList == null) {
                usersList = new ArrayList<>();
            }
        }

        playerScoreboardTable.getItems().clear();
        playerScoreboardTable.getItems().addAll(usersList); // Add users and their scores to the table

    }
    public void saveScoreToJson(String nickname, int winScore) {
        Gson gson = new Gson();
        Path jsonFilePath = Paths.get(FILE_PATH);

        try {
            // Load existing data
            if (Files.exists(jsonFilePath)) {
                String jsonContent = Files.readString(jsonFilePath);
                usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {}.getType());
                if (usersList == null) {
                    usersList = new ArrayList<>();
                }
            } else {
                usersList = new ArrayList<>();
            }

            // Update or add user
            boolean userExists = false;
            for (Users user : usersList) {
                if (user.getName().equalsIgnoreCase(nickname)) {
                    int newScore = user.getScore() + winScore;  // Add the new score to the existing score
                    user.setScore(newScore);                    // Update score with the cumulative value
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                usersList.add(new Users(nickname, winScore)); // If user is new, add them with initial winScore
            }

            // Write back the updated list
            String updatedJsonContent = gson.toJson(usersList);
            Files.writeString(jsonFilePath, updatedJsonContent);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving score to JSON file", e);
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

    // getters
    public AnchorPane getRoot() {
        return root;
    }

    public TableColumn<Users, String> getNicknameColum() {
        return nicknameColum;
    }

    public TableView<Users> getPlayerScoreboardTable() {
        return playerScoreboardTable;
    }

    public TableColumn<Users, Integer> getScoreColumn() {
        return scoreColumn;
    }
}
