package it.unicam.cs.mpgc.rpg129852.model;

import it.unicam.cs.mpgc.rpg129852.dto.Book;

import java.util.List;

public interface Inventory {
    void addBookId(String bookId);
    List<String> getBookIds();
    boolean contains(List<String> bookIds);
}
