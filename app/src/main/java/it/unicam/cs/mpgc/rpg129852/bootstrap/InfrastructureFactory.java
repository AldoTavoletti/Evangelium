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
import java.util.Objects;

/**
 * Factory class responsible for creating and configuring infrastructure-level components,
 * such as serialization engines, repositories, and data registries.
 */
class InfrastructureFactory {

    private static final String SAVE_FOLDER_NAME = ".evangelium";
    private static final String SAVES_SUBFOLDER = "saves";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private InfrastructureFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates and configures a Gson instance tailored for the application's serialization needs.
     *
     * @return a fully configured Gson instance
     */
    static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Inventory.class, (JsonDeserializer<Inventory>) (json, typeOfT, context) ->
                        context.deserialize(json, InventoryImpl.class))
                .create();
    }

    /**
     * Creates a game repository configured to store save files in the user's home directory.
     *
     * @param gson the Gson instance used for serialization and deserialization
     * @return a configured GameRepository
     * @throws NullPointerException if the gson instance is null
     */
    static GameRepository createGameRepository(Gson gson) {
        Objects.requireNonNull(gson, "The Gson instance must not be null.");

        String userHome = System.getProperty("user.home");
        Path saveDirectory = Paths.get(userHome, SAVE_FOLDER_NAME, SAVES_SUBFOLDER);
        return new JsonGameRepository(saveDirectory, gson);
    }

    /**
     * Loads a resource registry from a specified path and parses its contents.
     *
     * @param path the internal path to the resource file
     * @param type the class type of the resources to be loaded
     * @param gson the Gson instance used for deserialization
     * @param <T>  the generic type extending Resource
     * @return a fully loaded ResourceRegistry
     * @throws NullPointerException if any of the parameters are null
     */
    static <T extends Resource> ResourceRegistry<T> loadRegistry(String path, Class<T> type, Gson gson) {
        Objects.requireNonNull(path, "The path must not be null.");
        Objects.requireNonNull(type, "The class type must not be null.");
        Objects.requireNonNull(gson, "The Gson instance must not be null.");

        ResourceRegistry<T> registry = new ResourceRegistryImpl<>(path, type, gson);
        registry.loadResources();
        return registry;
    }
}