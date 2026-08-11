package it.unicam.cs.mpgc.rpg129852.persistence.game;

public class SaveCorruptedException extends RuntimeException {
    public SaveCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveCorruptedException(String message) {
        super(message);
    }
}
