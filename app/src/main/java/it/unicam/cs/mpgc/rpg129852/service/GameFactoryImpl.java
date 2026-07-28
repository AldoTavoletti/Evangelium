package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

public class GameFactoryImpl implements GameFactory {

    @Override
    public Game create(NewGameRequest request) {
        DiscipleData discipleData = new DiscipleData(request.discipleName(), request.job(), request.color());
        GameState gameState = new GameState(discipleData);
        return new Game(request.saveName(), gameState);
    }
}
