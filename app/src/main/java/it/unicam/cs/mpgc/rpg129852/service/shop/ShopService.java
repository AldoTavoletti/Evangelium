package it.unicam.cs.mpgc.rpg129852.service.shop;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;

public interface ShopService {
    void buy(Book book);
    Virtues getAvailableVirtues();
    List<Book> getAvailableBooks();

    boolean canAfford(Book book);
}
