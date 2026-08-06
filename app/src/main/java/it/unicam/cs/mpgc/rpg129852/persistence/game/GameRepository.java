package it.unicam.cs.mpgc.rpg129852.persistence.game;

import it.unicam.cs.mpgc.rpg129852.model.game.Game;

/**
 * Handles the persistence lifecycle of {@link Game} entities.
 *
 * Provides the core mechanisms to store, retrieve, and delete game data
 * from the underlying storage system.
 */
public interface GameRepository extends AvailableSavesProvider {

    /**
     * Persists the given game state to the storage.
     *
     * If a save file with the same name already exists in the storage,
     * it will be silently overwritten.
     *
     * @param game the game instance to be persisted.
     */
    void save(Game game);

    /**
     * Reconstructs a game instance from the storage using the specified save name.
     *
     * @param saveName the unique name of the save to load (without the extension).
     * @return the fully reconstructed game instance.
     * @throws IllegalArgumentException if the specified save data does not exist.
     * @throws RuntimeException if the storage cannot be read or the data is corrupted.
     */
    Game load(String saveName);

    /**
     * Permanently removes the save data associated with the given name from the storage.
     *
     * @param saveName the name of the save to delete.
     * @throws IllegalArgumentException if the specified save data does not exist.
     * @throws RuntimeException if an I/O error occurs during the deletion process.
     */
    void delete(String saveName);
}