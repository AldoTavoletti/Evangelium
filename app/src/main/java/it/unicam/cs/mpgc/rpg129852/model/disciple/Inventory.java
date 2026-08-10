package it.unicam.cs.mpgc.rpg129852.model.disciple;

import java.util.List;

/**
 * Defines the contract for managing the player's inventory.
 * It tracks the items (books) acquired by the disciple and provides operations
 * to add new items and verify current possessions.
 */
public interface Inventory {

    /**
     * Adds a new book identifier to the inventory.
     *
     * @param bookId the unique identifier of the book to add
     * @throws IllegalArgumentException if the bookId is null or blank
     */
    void addBookId(String bookId);

    /**
     * Retrieves all the book identifiers currently present in the inventory.
     *
     * @return an unmodifiable list of book IDs
     */
    List<String> getBookIds();

    /**
     * Checks if the inventory contains all the specified book identifiers.
     *
     * @param bookIds the list of book IDs to check
     * @return true if all books are present, false otherwise
     * @throws NullPointerException if the provided list is null
     */
    boolean contains(List<String> bookIds);

    /**
     * Checks if the inventory contains the specified book identifier.
     *
     * @param bookId the book ID to check
     * @return true if the book is present, false otherwise
     * @throws IllegalArgumentException if the bookId is null or blank
     */
    boolean contains(String bookId);
}