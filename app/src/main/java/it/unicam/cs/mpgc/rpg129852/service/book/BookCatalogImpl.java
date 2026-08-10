package it.unicam.cs.mpgc.rpg129852.service.book;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.List;

public class BookCatalogImpl implements BookCatalog {

    private final ResourceRegistry<Book> registry;

    public BookCatalogImpl(ResourceRegistry<Book> registry) {
        this.registry = registry;
    }

    @Override
    public List<Book> getAllBooks() {
        return List.copyOf(registry.getAllResources());
    }

    @Override
    public List<Book> getBooksFromIds(List<String> bookIds) {
        return registry.getAllResources().stream()
                .filter(book -> bookIds.contains(book.id()))
                .toList();
    }

    @Override
    public List<String> getBookNamesFromIds(List<String> bookIds) {
        return registry.getAllResources().stream()
                .filter(book -> bookIds.contains(book.id()))
                .map(Book::displayName)
                .toList();
    }
}
