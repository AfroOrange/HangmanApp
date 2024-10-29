package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SecretWord {
    private final String word;
    private final StringProperty hiddenWord;

    public SecretWord(String word) {
        this.word = word;
        this.hiddenWord = new SimpleStringProperty(generateHiddenWord(word));
    }

    private String generateHiddenWord(String word) {
        // Replace letters with underscores and keep spaces
        StringBuilder hiddenWordBuilder = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (c == ' ') {
                hiddenWordBuilder.append(" "); // Keep spaces as is
            } else {
                hiddenWordBuilder.append("_"); // Replace other characters with underscores
            }
        }
        return hiddenWordBuilder.toString();
    }

    public StringProperty hiddenWordProperty() {
        return hiddenWord;
    }

    public String getHiddenWord() {
        return hiddenWord.get();
    }

    public void updateHiddenWord() {
        hiddenWord.set(hiddenWord.get());
    }

    public int guessLetter(char letter) {
        int appearances = 0;
        StringBuilder newHiddenWord = new StringBuilder(hiddenWord.get());
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                newHiddenWord.setCharAt(i, letter); // Replace underscores with the correct letter
                appearances++;
            }
        }
        hiddenWord.set(newHiddenWord.toString()); // Update the hidden word property
        return appearances;
    }

    public String getWord() {
        return word;
    }
}
