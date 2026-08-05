package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.service.NewGameRequest;

public class GameFactoryImpl implements GameFactory {

    @Override
    public Game create(NewGameRequest request) {
        DiscipleData discipleData = new DiscipleData(request.discipleName(), request.job(), request.color());
        Inventory inventory = new InventoryImpl();
        GameState gameState = new GameState(discipleData, inventory);
        return new Game(request.saveName(), gameState);
    }
}
