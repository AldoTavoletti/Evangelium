package it.unicam.cs.mpgc.rpg129852.service.save;

public class SaveAlreadyExistsException extends RuntimeException {
    public SaveAlreadyExistsException(String message) {
        super(message);
    }
}
