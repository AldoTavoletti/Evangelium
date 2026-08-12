package it.unicam.cs.mpgc.rpg129852.model.disciple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link Inventory}.
 * It safely manages a mutable list of acquired books, ensuring that external
 * components cannot directly modify the internal state without using the exposed methods.
 */
public class InventoryImpl implements Inventory {

    private final List<String> bookIds;

    /**
     * Constructs a new, empty inventory.
     * This is also the default constructor invoked by Gson during deserialization.
     */
    public InventoryImpl() {
        this.bookIds = new ArrayList<>();
    }

    @Override
    public void addBookId(String bookId) {
        validateBookId(bookId);
        this.bookIds.add(bookId);
    }

    @Override
    public List<String> getBookIds() {
        return List.copyOf(this.bookIds);
    }

    @Override
    public boolean contains(List<String> bookIdsToCheck) {
        Objects.requireNonNull(bookIdsToCheck, "The list of book IDs to check must not be null.");

        if (bookIdsToCheck.isEmpty()) {
            return true;
        }

        return this.bookIds.containsAll(bookIdsToCheck);
    }

    @Override
    public boolean contains(String bookId) {
        validateBookId(bookId);
        return this.bookIds.contains(bookId);
    }

    private void validateBookId(String bookId) {
        if (bookId == null || bookId.isBlank()) {
            throw new IllegalArgumentException("The book ID must not be null or blank.");
        }
    }
}