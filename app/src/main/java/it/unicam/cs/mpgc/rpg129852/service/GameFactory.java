package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.Game;

public interface GameFactory {
    public Game create(NewGameRequest request, String saveName);
}
