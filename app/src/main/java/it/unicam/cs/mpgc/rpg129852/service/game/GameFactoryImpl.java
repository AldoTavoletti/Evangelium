package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.disciple.InventoryImpl;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameStateImpl;

import java.util.Objects;

/**
 * Concrete implementation of the {@link GameFactory} interface.
 * It creates a brand-new game using the data chosen by the user during the disciple creation.
 * It is not used everytime a {@link Game} is instantiated, but only when a new disciple and saving is created.
 */
public class GameFactoryImpl implements GameFactory {

    @Override
    public Game create(NewGameRequest request) {
        Objects.requireNonNull(request, "The provided request must not be null.");

        DiscipleData discipleData = new DiscipleData(request.discipleName(), request.job(), request.color());
        Inventory inventory = new InventoryImpl();
        GameStateImpl gameState = new GameStateImpl(discipleData, inventory);
        return new Game(request.saveName(), gameState);
    }
}
