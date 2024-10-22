package windows.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import tab.controllers.GameController;
import users.Users;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField nicknameField;

    @FXML
    private BorderPane root;

    private MediaPlayer mediaPlayer;

    @FXML
    void onLoginAction(ActionEvent event) {
        Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);

        if (nicknameField.getText().isEmpty()) {
            loginAlert.setContentText("Please enter a nickname");
            loginAlert.showAndWait();
        } else {
            if (mediaPlayer != null) { // Se detiene la música de fondo
                mediaPlayer.stop();
            }
            // se llama al método que guarda los datos del usuario
            try {
                saveScoreBoardData();
            } catch (IOException e) {
                loginAlert.setAlertType(Alert.AlertType.ERROR);
                loginAlert.setContentText("Error saving user data");
                loginAlert.showAndWait();
                return;
            }

            // Se produce un mensaje de bienvenida con el nickname introducido
            loginAlert.setTitle("Login");
            loginAlert.setHeaderText("Welcome " + nicknameField.getText());
            loginAlert.setContentText("Hangman game is about to start");
            loginAlert.showAndWait();

            GameController gameController = new GameController();
            gameController.setNickname(nicknameField.getText()); // Se establece el nickname en el usuario

            RootController rootController = new RootController(gameController);

            Stage stage = new Stage();
            stage.setTitle("Hangman");
            stage.setScene(new Scene(rootController.getRoot(), 600, 450));
            stage.show();

            // Se cierra la ventana de login
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    public LoginController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginSceneView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playBackgroundMusic();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void saveScoreBoardData() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File jsonFile = new File("users/users.json");
        List<Users> usersList = new ArrayList<>();

        // Comprueba si existe el archivo JSON, si no, crea una lista vacía
        if (jsonFile.exists()) {
            String jsonContent = Files.readString(jsonFile.toPath());
            if (!jsonContent.isEmpty()) {
                usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {
                }.getType());
            }
        }

        // Crea un nuevo usuario y lo agrega a la lista dado el nickname
        Users newUser = new Users();
        newUser.setName(nicknameField.getText());

        usersList.add(newUser);
        String json = gson.toJson(usersList);

        // Se asegura que el archivo Json existe y se puede escribir
        try {
            Files.writeString(jsonFile.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to write user data to JSON file", e);
        }
    }
    private void playBackgroundMusic() {
        String musicPath = Objects.requireNonNull(getClass().getResource("/music/socorro.mp3")).toExternalForm();

        mediaPlayer = new MediaPlayer(new Media(musicPath));
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaPlayer.play();
    }
    public TextField getNicknameField() {
        return nicknameField;
    }
}
