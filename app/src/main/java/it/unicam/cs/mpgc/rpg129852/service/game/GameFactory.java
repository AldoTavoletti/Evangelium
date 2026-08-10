package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.model.game.Game;

/**
 * Creates a new game based on the data chosen by the user during the disciple creation.
 * <p>
 * It is not used everytime a game is instantiated (for example, when a game is loaded), but only when a brand-new
 * game is created after the disciple creation.
 */
public interface GameFactory {

    /**
     *
     * @param request the data chosen by the user during the creation of the disciple.
     * @return the created {@link Game}
     * @throws NullPointerException if the provided request is null
     */
    Game create(NewGameRequest request);
}
