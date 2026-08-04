package it.unicam.cs.mpgc.rpg129852.model;

import it.unicam.cs.mpgc.rpg129852.dto.Book;

import java.util.ArrayList;
import java.util.List;

public record InventoryImpl(List<Book> books) implements Inventory {

    // Compact constructor: fondamentale per Gson!
    // Intercetta la creazione dell'oggetto e forza la lista ad essere un'ArrayList mutabile.
    public InventoryImpl {
        books = new ArrayList<>(books != null ? books : new ArrayList<>());
    }

    public InventoryImpl() {
        this(new ArrayList<>());
    }

    @Override
    public void addBook(Book bookToAdd) {
        books.add(bookToAdd);
    }

    @Override
    public boolean contains(List<Book> booksToCheck) {
        return this.books.containsAll(booksToCheck);
    }
}