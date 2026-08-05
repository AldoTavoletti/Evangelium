package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;
import java.util.Optional;

public class PlayerMenuServiceImpl implements PlayerMenuService {

    private static final String ERR_NO_SESSION = "Ci deve essere un contesto di gioco attivo per accedere al menu giocatore.";

    private final GameSessionManager sessionManager;
    private final BookCatalog bookCatalog;
    private final LevelCatalog levelCatalog;
    private final LevelStarter levelStarter;

    public PlayerMenuServiceImpl(GameSessionManager sessionManager,
                                 BookCatalog bookCatalog,
                                 LevelCatalog levelCatalog,
                                 LevelStarter levelStarter) {
        this.sessionManager = sessionManager;
        this.bookCatalog = bookCatalog;
        this.levelCatalog = levelCatalog;
        this.levelStarter = levelStarter;
    }

    @Override
    public void validateSession() {
        if (!sessionManager.hasActiveGame()) {
            throw new IllegalStateException(ERR_NO_SESSION);
        }
    }

    @Override
    public DiscipleData getCurrentDiscipleData() {
        return sessionManager.getCurrentDiscipleData();
    }

    @Override
    public int getTotalVirtues() {
        return getCurrentDiscipleData().getTotalVirtues();
    }

    @Override
    public List<LevelMetadata> getLevelsByCategory(LevelCategory category) {
        return levelCatalog.getLevelsByCategory(category);
    }

    @Override
    public void startLevel(LevelMetadata level) {
        levelStarter.start(level);
    }

    @Override
    public Optional<Virtues> getScoreForLevel(String levelId) {
        // La Legge di Demetra è confinata qui, il controller non ne saprà nulla.
        return sessionManager.getCurrentGame().gameState().getScoreForLevel(levelId);
    }

    @Override
    public boolean hasRequiredBooks(List<String> requiredBookIds) {
        return sessionManager.getCurrentGame().gameState().getInventory().contains(requiredBookIds);
    }

    @Override
    public String getFormattedRequiredBookNames(List<String> requiredBookIds) {
        if (requiredBookIds == null || requiredBookIds.isEmpty()) {
            return "Nessun requisito";
        }

        List<String> bookNames = bookCatalog.getBookNamesFromIds(requiredBookIds);

        if (bookNames.isEmpty()) {
            return "Nessun requisito";
        }

        return String.join(", ", bookNames);
    }
}