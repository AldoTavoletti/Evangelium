package it.unicam.cs.mpgc.rpg129852.service.shop;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;

import java.util.List;

public class ShopServiceImpl implements ShopService {

    private final BookCatalog bookCatalog;
    private final GameProvider gameProvider;
    private final GameRepository repository;

    public ShopServiceImpl(BookCatalog bookCatalog, GameProvider gameProvider, GameRepository repository) {
        this.bookCatalog = bookCatalog;
        this.gameProvider = gameProvider;
        this.repository = repository;
    }

    @Override
    public void buy(Book book) {
        if (!canAfford(book)) {
            throw new IllegalStateException("Non hai abbastanza virtù per acquistare questo libro.");
        }

        Game currentGame = gameProvider.getCurrentGame();
        GameState gameState = currentGame.gameState();

        if (gameState.getInventory().contains(book.id())) {
            throw new IllegalStateException("Possiedi già questo libro nel tuo inventario.");
        }

        gameState.getDiscipleData().subtractVirtues(book.price());
        gameState.getInventory().addBookId(book.id());

        repository.save(currentGame);
    }

    @Override
    public List<Book> getAvailableBooks() {
        List<Book> allBooks = bookCatalog.getAllBooks();
        List<String> boughtBookIds = getCurrentGameState().getInventory().getBookIds();

        return allBooks.stream()
                .filter(book -> !boughtBookIds.contains(book.id()))
                .toList();
    }

    @Override
    public boolean canAfford(Book book) {
        return getAvailableVirtues().isGreaterThanOrEqualTo(book.price());
    }

    private Virtues getAvailableVirtues() {
        return getCurrentGameState().getDiscipleData().getVirtues();
    }


    private GameState getCurrentGameState() {
        return gameProvider.getCurrentGame().gameState();
    }
}