package it.unicam.cs.mpgc.rpg129852.model.disciple;

import java.util.ArrayList;
import java.util.List;

public record InventoryImpl(List<String> bookIds) implements Inventory {

    // Compact constructor: fondamentale per Gson!
    // Intercetta la creazione dell'oggetto e forza la lista ad essere un'ArrayList mutabile.
    public InventoryImpl {
        bookIds = new ArrayList<>(bookIds != null ? bookIds : new ArrayList<>());
    }

    public InventoryImpl() {
        this(new ArrayList<>());
    }

    @Override
    public void addBookId(String bookId) {
        bookIds.add(bookId);
    }

    @Override
    public List<String> getBookIds() {
        return List.copyOf(bookIds);
    }

    @Override
    public boolean contains(List<String> bookIdsToCheck) {
        return this.bookIds.containsAll(bookIdsToCheck);
    }
}