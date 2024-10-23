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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import tab.controllers.GameController;
import models.Users;

import javafx.scene.media.Media;
import tab.controllers.ScoreBoardController;

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

    private Image appIcon;

    private MediaPlayer mediaPlayer;

    @FXML
    void onLoginAction(ActionEvent event) {
        Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);

        if (nicknameField.getText().isEmpty()) {
            loginAlert.setContentText("Please enter a nickname");
            loginAlert.showAndWait();
        } else {
            // Cancela la reproducción de música de fondo
            if (mediaPlayer != null) {
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
            
            // llama al controlador de la aplicación
            loginScene();

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
        String nickname = nicknameField.getText();
        ScoreBoardController scoreBoardController = new ScoreBoardController();

        // Check if the username already exists
        if (scoreBoardController.checkUsername(nickname)) {
            Alert duplicateNickname = new Alert(Alert.AlertType.INFORMATION);
            duplicateNickname.setTitle("Login");
            duplicateNickname.setContentText("Welcome back: " + nicknameField.getText() + "!");
            duplicateNickname.showAndWait();

        } else {

            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);

            // Se produce un mensaje de bienvenida con el nickname introducido
            loginAlert.setTitle("Login");
            loginAlert.setHeaderText("Welcome: " + nicknameField.getText() + "!");
            loginAlert.setContentText("Hangman game is about to start");
            loginAlert.showAndWait();

            // Existing code for adding the new user if the username is unique
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File jsonFile = new File("users/users.json");
            List<Users> usersList = new ArrayList<>();

            if (jsonFile.exists()) {
                String jsonContent = Files.readString(jsonFile.toPath());
                if (!jsonContent.isEmpty()) {
                    usersList = gson.fromJson(jsonContent, new TypeToken<List<Users>>() {
                    }.getType());
                }
            }
            Users newUser = new Users();
            newUser.setName(nickname);
            usersList.add(newUser);

            String json = gson.toJson(usersList);
            Files.writeString(jsonFile.toPath(), json);
        }
    }


    public TextField getNicknameField() {
        return nicknameField;
    }

    private void playBackgroundMusic() {
        // Locate the media file (you can use the classloader to find it in resources)
        String musicPath = Objects.requireNonNull(getClass().getResource("/music/login_background_music_oria.wav")).toExternalForm();

        // Create the Media and MediaPlayer objects
        mediaPlayer = new MediaPlayer(new Media(musicPath));

        mediaPlayer.setVolume(0.1);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Start playing the music
        mediaPlayer.play();
    }

    private void loginScene() {

        GameController gameController = new GameController();
        gameController.setNickname(nicknameField.getText()); // Se establece el nickname en el usuario
        RootController rootController = new RootController(gameController);

        // añade el stage y el icono de la aplicación
        appIcon = new Image(getClass().getResource("/images/9.png").toString());
        Stage stage = new Stage();

        stage.getIcons().add(appIcon);
        stage.setTitle("Hangman");
        stage.setScene(new Scene(rootController.getRoot()));
        stage.show();
    }
}
