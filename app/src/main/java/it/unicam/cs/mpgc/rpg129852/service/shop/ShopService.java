package it.unicam.cs.mpgc.rpg129852.service.shop;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;

import java.util.List;

/**
 * Defines the contract for the in-game shop mechanics.
 * Handles the logic for purchasing books, verifying affordability,
 * and querying the catalog for items the player has not yet acquired.
 */
public interface ShopService {

    /**
     * Processes the purchase of a specific book.
     * The implementation must deduct the cost from the player's currency
     * and add the book to the player's inventory.
     *
     * @param book the book to be purchased
     * @throws IllegalStateException if the player cannot afford the book or already owns it
     * @throws NullPointerException if the provided book is null
     */
    void buy(Book book);

    /**
     * Checks whether the player currently has enough currency to buy the specified book.
     *
     * @param book the book to check affordability for
     * @return true if the player has enough currency, false otherwise
     * @throws NullPointerException if the provided book is null
     */
    boolean canAfford(Book book);

    /**
     * Retrieves a list of all books from the catalog that the player does not currently own.
     *
     * @return a list of books available for purchase
     */
    List<Book> getAvailableBooks();
}