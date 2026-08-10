package it.unicam.cs.mpgc.rpg129852.service.book;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link BookCatalog} interface.
 * It uses a {@link ResourceRegistry} to access the underlying book data.
 */
public class BookCatalogImpl implements BookCatalog {

    private final ResourceRegistry<Book> registry;

    /**
     * Constructs a new catalog using the specified resource registry.
     *
     * @param registry the data source containing the book entities
     * @throws NullPointerException if the provided registry is null
     */
    public BookCatalogImpl(ResourceRegistry<Book> registry) {
        this.registry = Objects.requireNonNull(registry, "The resource registry must not be null.");
    }

    @Override
    public List<Book> getAllBooks() {
        return List.copyOf(registry.getAllResources());
    }

    @Override
    public List<String> getBookNamesFromIds(List<String> bookIds) {
        Objects.requireNonNull(bookIds, "The list of book IDs must not be null.");

        return registry.getAllResources().stream()
                .filter(book -> bookIds.contains(book.id()))
                .map(Book::displayName)
                .toList();
    }
}