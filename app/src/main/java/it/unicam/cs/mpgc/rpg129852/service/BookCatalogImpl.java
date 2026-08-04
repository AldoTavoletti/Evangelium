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

    public List<Book> getNotBoughtBooks(List<Book> boughtBooks) {
        List<Book> allBooks = registry.getAllResources();

        return allBooks.stream()
                .filter(book -> !boughtBooks.contains(book))
                .toList();
    }

}
