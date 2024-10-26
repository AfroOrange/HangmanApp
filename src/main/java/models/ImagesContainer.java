package models;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImagesContainer {
    private final Map<Integer, Image> hangmanImages = new HashMap<>();

    public ImagesContainer () {
        loadImages();
    }

    private void loadImages() {
        // Add images with specific indexes
        hangmanImages.put(1, new Image(String.valueOf(getClass().getResource("/images/1.png"))));
        hangmanImages.put(2, new Image(String.valueOf(getClass().getResource("/images/2.png"))));
        hangmanImages.put(3, new Image(String.valueOf(getClass().getResource("/images/3.png"))));
        hangmanImages.put(4, new Image(String.valueOf(getClass().getResource("/images/4.png"))));
        hangmanImages.put(5, new Image(String.valueOf(getClass().getResource("/images/5.png"))));
        hangmanImages.put(6, new Image(String.valueOf(getClass().getResource("/images/6.png"))));
        hangmanImages.put(7, new Image(String.valueOf(getClass().getResource("/images/7.png"))));
        hangmanImages.put(8, new Image(String.valueOf(getClass().getResource("/images/8.png"))));
        hangmanImages.put(9, new Image(String.valueOf(getClass().getResource("/images/9.png"))));
    }

    // mostrar la imagen a través del índice
    public Image getImage(int index) {
        return hangmanImages.get(index);
    }
}
