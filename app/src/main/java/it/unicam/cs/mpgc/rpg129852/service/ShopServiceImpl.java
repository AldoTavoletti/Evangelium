package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

import java.util.List;

public class ShopServiceImpl implements ShopService {

    private final GameSessionManager gameSessionManager;
    private final GameRepository repository;

    public ShopServiceImpl(GameSessionManager gameSessionManager, GameRepository repository) {
        this.gameSessionManager = gameSessionManager;
        this.repository = repository;
    }

    public void buy(Book book) {
        Game game = gameSessionManager.getCurrentGame();
        game.gameState().getInventory().addBook(book);

        repository.save(game);
    }

    @Override
    public Virtues getAvailableVirtues() {
        return gameSessionManager.getCurrentDiscipleData().getVirtues();
    }

    @Override
    public List<Book> getBoughtBooks() {
        return gameSessionManager.getCurrentGame().gameState().getInventory().books();
    }

}
