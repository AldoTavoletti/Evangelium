package it.unicam.cs.mpgc.rpg129852.persistence;

public class SaveCorruptedException extends RuntimeException {
    public SaveCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
