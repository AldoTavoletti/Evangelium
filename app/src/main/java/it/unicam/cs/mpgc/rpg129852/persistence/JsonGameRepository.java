package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class JsonGameRepository implements GameRepository {

    private final Gson gson;

    public JsonGameRepository(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void save(Game game) {

        try (Writer writer = new FileWriter(game.getSavePath())) {
            gson.toJson(game.getGameState(), writer);
        } catch (IOException e) {
            System.err.println("Error during game saving: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public GameState load(String saveFileName) {
        return null;
    }

    @Override
    public List<String> getAvailableSaves() {
        return List.of();
    }
}