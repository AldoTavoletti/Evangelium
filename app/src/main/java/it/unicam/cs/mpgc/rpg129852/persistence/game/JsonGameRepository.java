package it.unicam.cs.mpgc.rpg129852.persistence.game;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.game.GameStateImpl;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Concrete implementation of the {@link GameRepository}.
 * It persists the game state as JSON files on the local file system.
 * Each save file is dynamically named after the game's save name with a ".json" extension.
 */
public class JsonGameRepository implements GameRepository {

    private static final String EXTENSION = ".json";
    private final Gson gson;
    private final Path saveDirectory;

    /**
     * Constructs a new JSON-based game repository.
     * If the specified save directory does not currently exist on the file system,
     * it will be created automatically upon instantiation.
     *
     * @param saveDirectory the base directory where save files will be stored
     * @param gson          the Gson instance configured for serializing and deserializing the game state
     * @throws NullPointerException if the save directory or Gson instance is null
     * @throws GameStorageException if the save directory cannot be created
     */
    public JsonGameRepository(Path saveDirectory, Gson gson) {
        this.saveDirectory = Objects.requireNonNull(saveDirectory, "The save directory must not be null.");
        this.gson = Objects.requireNonNull(gson, "The Gson instance must not be null.");

        initializeDirectory();
    }

    @Override
    public void save(Game game) {
        Objects.requireNonNull(game, "The game to save must not be null.");

        Path fullPath = saveDirectory.resolve(game.saveName() + EXTENSION);
        setGameState(game.gameState(), fullPath);
    }

    @Override
    public Game load(String saveName) {
        Objects.requireNonNull(saveName, "The save name must not be null.");

        Path fullPath = resolveExistingPath(saveName);
        GameStateImpl loadedState = getGameState(fullPath);

        return new Game(saveName, loadedState);
    }

    @Override
    public void delete(String saveName) {
        Objects.requireNonNull(saveName, "The save name must not be null.");

        Path fullPath = resolveExistingPath(saveName);
        executeDeletion(fullPath);
    }

    @Override
    public List<String> getAvailableSaves() {
        try (Stream<Path> paths = Files.list(saveDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> path.getFileName().toString())
                    .map(fileName -> fileName.substring(0, fileName.lastIndexOf('.')))
                    .toList();

        } catch (IOException e) {
            throw new GameStorageException("Cannot read the save directory.", e);
        }
    }

    private void setGameState(GameState gameState, Path fullPath) {
        try (Writer writer = Files.newBufferedWriter(fullPath, StandardCharsets.UTF_8)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            throw new GameStorageException("Cannot save the game in this path: " + fullPath, e);
        }
    }

    private @NonNull GameStateImpl getGameState(Path fullPath) {
        try (Reader reader = Files.newBufferedReader(fullPath, StandardCharsets.UTF_8)) {
            GameStateImpl state = gson.fromJson(reader, GameStateImpl.class);

            if (state == null) {
                throw new SaveCorruptedException("The save file is empty or missing valid state data: " + fullPath);
            }

            return state;
        } catch (JsonParseException e) {
            throw new SaveCorruptedException("The save file is corrupted or invalid: " + fullPath, e);
        } catch (IOException e) {
            throw new GameStorageException("Cannot read the save file at path: " + fullPath, e);
        }
    }

    private void executeDeletion(Path fullPath) {
        try {
            Files.delete(fullPath);
        } catch (IOException e) {
            throw new GameStorageException("Cannot delete save file at this path: " + fullPath, e);
        }
    }

    private Path resolveExistingPath(String saveName) {
        Path fullPath = saveDirectory.resolve(saveName + EXTENSION);

        if (!Files.exists(fullPath)) {
            throw new SaveNotFoundException("The saving doesn't exist: " + saveName);
        }

        return fullPath;
    }

    private void initializeDirectory() {
        try {
            Files.createDirectories(saveDirectory);
        } catch (IOException e) {
            throw new GameStorageException("Cannot create the save directory in: " + saveDirectory, e);
        }
    }
}