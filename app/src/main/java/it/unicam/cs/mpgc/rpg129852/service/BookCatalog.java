package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.Book;

import java.util.List;

public interface BookCatalog {
    public List<Book> getNotBoughtBooks(List<Book> boughtBooks);
}
