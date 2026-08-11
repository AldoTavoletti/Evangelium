package it.unicam.cs.mpgc.rpg129852.service.shop;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link ShopService}.
 * Orchestrates the interaction between the book catalog, the current game session,
 * and the persistence layer to manage shop transactions safely.
 */
public class ShopServiceImpl implements ShopService {

    private final BookCatalog bookCatalog;
    private final GameProvider gameProvider;
    private final GameRepository repository;

    /**
     * Constructs a new shop service with the necessary dependencies.
     *
     * @param bookCatalog  the catalog providing access to all existing books
     * @param gameProvider the provider granting access to the current game session
     * @param repository   the repository used to persist changes after a purchase
     * @throws NullPointerException if any of the dependencies are null
     */
    public ShopServiceImpl(BookCatalog bookCatalog, GameProvider gameProvider, GameRepository repository) {
        this.bookCatalog = Objects.requireNonNull(bookCatalog, "The book catalog must not be null.");
        this.gameProvider = Objects.requireNonNull(gameProvider, "The game provider must not be null.");
        this.repository = Objects.requireNonNull(repository, "The game repository must not be null.");
    }

    @Override
    public void buy(Book book) {
        Objects.requireNonNull(book, "The book to purchase must not be null.");

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
        Objects.requireNonNull(book, "The book to check must not be null.");
        return getAvailableVirtues().isGreaterThanOrEqualTo(book.price());
    }

    private Virtues getAvailableVirtues() {
        return getCurrentGameState().getDiscipleData().getVirtues();
    }

    private GameState getCurrentGameState() {
        return gameProvider.getCurrentGame().gameState();
    }
}