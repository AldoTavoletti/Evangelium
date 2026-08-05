package it.unicam.cs.mpgc.rpg129852.service.game;

/**
 * Orchestrates the loading of a pre-existent game.
 *
 * This service ensures that the selected game is retrieved from the storage
 * and gets set as the active session.
 */
public interface GameLoader {

    /**
     *
     * @param saveName the name of the save file to load (just the name, not the full path). The extension of the file
     *                 must not be included in it.
     */
    public void loadGame(String saveName);
}
