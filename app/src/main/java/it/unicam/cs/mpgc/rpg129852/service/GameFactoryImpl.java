package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

public class GameFactoryImpl implements GameFactory {

    public Game create(String savePath, GameState gameState) {
        return new Game(savePath, gameState);
    }

}
