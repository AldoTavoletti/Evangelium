package it.unicam.cs.mpgc.rpg129852.service.shop;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;

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
        List<Book> allBooks = bookCatalog.getAllBooks();
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
