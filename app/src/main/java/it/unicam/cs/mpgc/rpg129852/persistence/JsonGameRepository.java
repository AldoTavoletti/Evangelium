package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonGameRepository implements GameRepository {

    private final String EXTENSION = ".json";
    private final Gson gson;
    private final Path saveDirectory;

    public JsonGameRepository(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        String userHome = System.getProperty("user.home");
        this.saveDirectory = Paths.get(userHome, ".evangelium", "saves");
        initializeDirectory();
    }

    private void initializeDirectory() {
        try {
            if (!Files.exists(saveDirectory)) {
                Files.createDirectories(saveDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create the save directory in: " + saveDirectory, e);
        }
    }

    @Override
    public void save(Game game) {
        String fileName = game.getSaveName() + EXTENSION;
        Path fullPath = saveDirectory.resolve(fileName);

        try (Writer writer = Files.newBufferedWriter(fullPath, StandardCharsets.UTF_8)) {
            gson.toJson(game.getGameState(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot save the game '" + game.getSaveName() + "' on disk.", e);
        }
    }

    @Override
    public Game load(String saveName) {
        Path fullPath = saveDirectory.resolve(saveName + EXTENSION);

        if (!Files.exists(fullPath)) {
            throw new IllegalArgumentException("The saving doesn't exist: " + saveName);
        }

        try (Reader reader = Files.newBufferedReader(fullPath, StandardCharsets.UTF_8)) {
            GameState loadedState = gson.fromJson(reader, GameState.class);
            return new Game(saveName, loadedState);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load the saving.", e);
        }
    }

    @Override
    public List<String> getAvailableSaves() {
        try (Stream<Path> paths = Files.list(saveDirectory)) {
            return paths
                    .filter(Files::isRegularFile) // exclude subdirectories
                    .filter(path -> path.toString().endsWith(".json")) // get only the JSON
                    .map(Path::getFileName) // get only the filename (es. "game1.json")
                    .map(Path::toString)
                    .map(fileName -> fileName.substring(0, fileName.length() - 5)) // remove the extension ".json"
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println("Cannot read the save directory: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}