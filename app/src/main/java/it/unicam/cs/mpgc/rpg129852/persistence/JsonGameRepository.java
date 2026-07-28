package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class JsonGameRepository implements GameRepository {

    private static final String EXTENSION = ".json";
    private final Gson gson;
    private final Path saveDirectory;

    public JsonGameRepository(Path saveDirectory) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.saveDirectory = saveDirectory;
        initializeDirectory();
    }

    @Override
    public void save(Game game) {
        Path fullPath = saveDirectory.resolve(game.getSaveName() + EXTENSION);
        setGameState(game.getGameState(), fullPath);
    }

    @Override
    public Game load(String saveName) {
        Path fullPath = resolveExistingPath(saveName);
        GameState loadedState = getGameState(fullPath);
        return new Game(saveName, loadedState);
    }

    @Override
    public void delete(String saveName) {
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
            throw new RuntimeException("Cannot read the save directory.", e);
        }
    }

    private void setGameState(GameState gameState, Path fullPath) {
        try (Writer writer = Files.newBufferedWriter(fullPath, StandardCharsets.UTF_8)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot save the game in this path: " + fullPath, e);
        }
    }

    private @NonNull GameState getGameState(Path fullPath) {
        try (Reader reader = Files.newBufferedReader(fullPath, StandardCharsets.UTF_8)) {
            GameState loadedState = gson.fromJson(reader, GameState.class);
            return loadedState;
        } catch (Exception e) {
            throw new RuntimeException("Cannot load the saving.", e);
        }
    }

    private void executeDeletion(Path fullPath) {
        try {
            Files.delete(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Cannot delete save file at this path:" + fullPath, e);
        }
    }

    private Path resolveExistingPath(String saveName) {
        Path fullPath = saveDirectory.resolve(saveName + EXTENSION);

        if (!Files.exists(fullPath)) {
            throw new IllegalArgumentException("The saving doesn't exist: " + saveName);
        }

        return fullPath;
    }

    private void initializeDirectory() {
        try {
            Files.createDirectories(saveDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create the save directory in: " + saveDirectory, e);
        }
    }

}