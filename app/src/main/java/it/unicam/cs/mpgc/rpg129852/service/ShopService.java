package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;

public interface ShopService {
    void buy(Book book);
    Virtues getAvailableVirtues();
    List<Book> getAvailableBooks();

    boolean canAfford(Book book);
}
