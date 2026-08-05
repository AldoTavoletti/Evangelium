package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.game.GameFactory;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarterImpl;
import it.unicam.cs.mpgc.rpg129852.service.save.SaveNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameStarterImplTest {

    private FakeGameRepository repository;
    private FakeSessionManager sessionManager;
    private StubGameFactory gameFactory;
    private StubSaveNameResolver nameResolver;
    private GameStarterImpl gameStarter;

    @BeforeEach
    void setUp() {
        repository = new FakeGameRepository();
        sessionManager = new FakeSessionManager();
        gameFactory = new StubGameFactory();
        nameResolver = new StubSaveNameResolver();
        gameStarter = new GameStarterImpl(repository, sessionManager, gameFactory, nameResolver);
    }

    @Test
    void startingNewGame_createsSaveAndActivatesSession() {
        givenTheResolverWillReturn("ArthurSave");
        Game expectedGame = givenTheFactoryWillCreateAGameNamed("ArthurSave");

        whenUserStartsNewGame("Arthur", "Knight", "Blue", "ArthurSave");

        thenGameIsSaved(expectedGame);
        thenGameIsActiveInSession(expectedGame);
    }

    @Test
    void overwritingGame_forcesNameResolutionAndActivatesSession() {
        givenTheResolverWillReturn("OldSave");
        Game expectedGame = givenTheFactoryWillCreateAGameNamed("OldSave");

        whenUserOverwritesGame("Lancelot", "Mage", "Red", "OldSave");

        thenGameIsSaved(expectedGame);
        thenGameIsActiveInSession(expectedGame);
    }

    private void givenTheResolverWillReturn(String resolvedName) {
        nameResolver.nameToReturn = resolvedName;
    }

    private Game givenTheFactoryWillCreateAGameNamed(String name) {
        DiscipleData data = new DiscipleData("DummyName", "DummyJob", "DummyColor");
        GameState state = new GameState(data);
        Game game = new Game(name, state);

        gameFactory.gameToReturn = game;
        return game;
    }

    private void whenUserStartsNewGame(String name, String job, String color, String saveName) {
        NewGameRequest request = new NewGameRequest(name, job, color, saveName);
        gameStarter.startNewGame(request);
    }

    private void whenUserOverwritesGame(String name, String job, String color, String saveName) {
        NewGameRequest request = new NewGameRequest(name, job, color, saveName);
        gameStarter.overwriteAndStartNewGame(request);
    }

    private void thenGameIsSaved(Game expectedGame) {
        assertNotNull(repository.savedGame, "Il gioco non e' stato salvato nel repository.");
        assertEquals(expectedGame, repository.savedGame, "Il gioco salvato non corrisponde a quello generato dalla factory.");
    }

    private void thenGameIsActiveInSession(Game expectedGame) {
        assertNotNull(sessionManager.activeGame, "Il gioco non e' stato attivato nella sessione.");
        assertEquals(expectedGame, sessionManager.activeGame, "Il gioco in sessione non corrisponde a quello generato dalla factory.");
    }

    class FakeGameRepository implements GameRepository {
        public Game savedGame;
        @Override public void save(Game game) { this.savedGame = game; }
        @Override public Game load(String name) { return null; }
        @Override public void delete(String name) {}
        @Override public List<String> getAvailableSaves() { return List.of(); }
    }

    class FakeSessionManager implements GameSessionManager {
        public Game activeGame;
        @Override public void setCurrentGame(Game game) { this.activeGame = game; }
        @Override public void clearSession() {}

        @Override
        public Game getCurrentGame() {
            return activeGame;
        }

        @Override
        public boolean hasActiveGame() {
            return activeGame != null;
        }
    }

    class StubSaveNameResolver implements SaveNameResolver {
        public String nameToReturn;
        @Override public String resolveFinalName(String baseName, boolean forceOverwrite) {
            return nameToReturn;
        }
    }

    class StubGameFactory implements GameFactory {
        public Game gameToReturn;
        @Override public Game create(NewGameRequest request, String finalSaveName) {
            return gameToReturn;
        }
    }
}