package it.unicam.cs.mpgc.rpg129852.service.book;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;

import java.util.List;

public interface BookCatalog {
    public List<Book> getBooks();

    public List<String> getBookIds();

    public List<Book> getBooksFromIds(List<String> bookIds);

    List<String> getBookNamesFromIds(List<String> bookIds);
}
