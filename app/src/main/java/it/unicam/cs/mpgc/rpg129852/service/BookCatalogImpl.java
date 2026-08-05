package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.List;

public class BookCatalogImpl implements BookCatalog {

    private final ResourceRegistry<Book> registry;

    public BookCatalogImpl(ResourceRegistry<Book> registry) {
        this.registry = registry;
    }

    @Override
    public List<Book> getBooks() {
        return List.copyOf(registry.getAllResources());
    }

    @Override
    public List<String> getBookIds() {
        return registry.getAllResources().stream()
                .map(Book::id)
                .toList();
    }

    @Override
    public List<Book> getBooksFromIds(List<String> bookIds) {
        return registry.getAllResources().stream()
                .filter(book -> bookIds.contains(book.id()))
                .toList();
    }
}
