package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SecretWord {
    private final String word;
    private final StringProperty hiddenWord;

    public SecretWord(String word) {
        this.word = word;
        this.hiddenWord = new SimpleStringProperty("_".repeat(word.length())); // Initialize with underscores
    }

    public StringProperty hiddenWordProperty() {
        return hiddenWord;
    }

    public String getHiddenWord() {
        return hiddenWord.get();
    }

    public void updateHiddenWord() {
        // Logic to update hiddenWord based on guesses
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
