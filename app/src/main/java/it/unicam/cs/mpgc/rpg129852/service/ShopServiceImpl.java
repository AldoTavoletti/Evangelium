package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

import java.util.List;

public class ShopServiceImpl implements ShopService {

    private final GameProvider gameProvider;
    private final GameRepository repository;
    private final BookCatalog bookCatalog;

    public ShopServiceImpl(BookCatalog bookCatalog, GameProvider gameProvider, GameRepository repository) {
        this.bookCatalog = bookCatalog;
        this.gameProvider = gameProvider;
        this.repository = repository;
    }

    public void buy(Book book) {
        Game game = gameProvider.getCurrentGame();
        game.gameState().getInventory().addBookId(book.id());

        DiscipleData discipleData = game.gameState().getDiscipleData();

        discipleData.subtractVirtues(book.price());

        repository.save(game);
    }

    @Override
    public Virtues getAvailableVirtues() {
        return gameProvider.getCurrentGame().gameState().getDiscipleData().getVirtues();
    }

    @Override
    public List<Book> getAvailableBooks() {
        List<Book> allBooks = bookCatalog.getBooks();
        List<String> boughtBookIds = gameProvider.getCurrentGame().gameState().getInventory().getBookIds();
        List<Book> boughtBooks = bookCatalog.getBooksFromIds(boughtBookIds);

        return allBooks.stream()
                .filter(book -> !boughtBooks.contains(book))
                .toList();
    }

    @Override
    public boolean canAfford(Book book) {
        return getAvailableVirtues().isGreaterThanOrEqualTo(book.price());
    }

}
