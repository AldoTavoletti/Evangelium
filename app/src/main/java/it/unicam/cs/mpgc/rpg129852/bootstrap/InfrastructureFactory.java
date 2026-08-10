package it.unicam.cs.mpgc.rpg129852.bootstrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.unicam.cs.mpgc.rpg129852.dto.Resource;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.disciple.InventoryImpl;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistryImpl;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.persistence.game.JsonGameRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

class InfrastructureFactory {

    private static final String SAVE_FOLDER_NAME = ".evangelium";
    private static final String SAVES_SUBFOLDER = "saves";

    static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Inventory.class, (JsonDeserializer<Inventory>) (json, typeOfT, context) ->
                        context.deserialize(json, InventoryImpl.class))
                .create();
    }

    static GameRepository createGameRepository(Gson gson) {
        String userHome = System.getProperty("user.home");
        Path saveDirectory = Paths.get(userHome, SAVE_FOLDER_NAME, SAVES_SUBFOLDER);
        return new JsonGameRepository(saveDirectory, gson);
    }

    static <T extends Resource> ResourceRegistry<T> loadRegistry(String path, Class<T> type, Gson gson) {
        ResourceRegistry<T> registry = new ResourceRegistryImpl<>(path, type, gson);
        registry.loadResources();
        return registry;
    }
}