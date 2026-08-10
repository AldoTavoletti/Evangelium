package it.unicam.cs.mpgc.rpg129852.model.disciple;

import java.util.List;

public interface Inventory {
    void addBookId(String bookId);
    List<String> getBookIds();
    boolean contains(List<String> bookIds);
    boolean contains(String bookId);
}
