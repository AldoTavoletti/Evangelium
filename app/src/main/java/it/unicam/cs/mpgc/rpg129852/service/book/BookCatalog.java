package it.unicam.cs.mpgc.rpg129852.service.book;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;

import java.util.List;

/**
 * Defines the contract for accessing the catalog of available books within the game.
 * It acts as a read-only repository, providing retrieval operations for {@link Book} entities.
 */
public interface BookCatalog {

    /**
     * Retrieves all available books from the catalog.
     *
     * @return a list containing all books, or an empty list if the catalog is empty (never returns null)
     */
    List<Book> getAllBooks();

    /**
     * Retrieves the display names of the books corresponding to the provided identifiers.
     * This is a convenience method designed to fetch UI-ready titles without needing
     * to process the full book objects.
     *
     * @param bookIds a list of unique book identifiers
     * @return a list containing the names of the matching books
     * @throws NullPointerException if the provided list of IDs is null
     */
    List<String> getBookNamesFromIds(List<String> bookIds);
}