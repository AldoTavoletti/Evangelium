package it.unicam.cs.mpgc.rpg129852.service;

public interface GameStarter {
    void startNewGame(NewGameRequest request);
    void overwriteAndStartNewGame(NewGameRequest request);
}