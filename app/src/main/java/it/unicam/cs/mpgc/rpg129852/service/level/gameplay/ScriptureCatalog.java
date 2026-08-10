package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

/**
 * Defines the contract for accessing the catalog of available scripture within the game.
 * It acts as a read-only repository.
 */
public interface ScriptureCatalog {

    /**
     * Retrieves the text of the specified scripture object.
     *
     * @param scriptureId   the ID of the scripture object from which the text needs to be retrieved
     * @return              the text of the scripture object
     * @throws NullPointerException if the provided scripture ID is null
     */
    String getScriptureText(String scriptureId);
}
