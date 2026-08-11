package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg129852.dto.Resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Concrete implementation of {@link ResourceRegistry}.
 * It loads JSON-formatted resources from the application's classpath and deserializes
 * them using Gson, storing them in a predictable order using a {@link LinkedHashMap}.
 *
 * @param <T> the specific type of resource managed by this registry
 */
public class ResourceRegistryImpl<T extends Resource> implements ResourceRegistry<T> {

    private final Map<String, T> resources = new LinkedHashMap<>();
    private final String jsonFilePath;
    private final Type listType;
    private final Gson gson;

    /**
     * Constructs a new resource registry.
     *
     * @param jsonFilePath       the classpath location of the JSON file containing the resources
     * @param typeParameterClass the class of the generic type T, required by Gson for accurate deserialization
     * @param gson               the configured Gson instance used for parsing the JSON data
     * @throws NullPointerException if any of the provided arguments are null
     */
    public ResourceRegistryImpl(String jsonFilePath, Class<T> typeParameterClass, Gson gson) {
        this.jsonFilePath = Objects.requireNonNull(jsonFilePath, "The JSON file path must not be null.");
        this.gson = Objects.requireNonNull(gson, "The Gson instance must not be null.");

        Objects.requireNonNull(typeParameterClass, "The type parameter class must not be null.");
        this.listType = TypeToken.getParameterized(List.class, typeParameterClass).getType();
    }

    @Override
    public void loadResources() {
        InputStream inputStream = getClass().getResourceAsStream(jsonFilePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("JSON file not found in the classpath: " + jsonFilePath);
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            List<T> loadedResources = gson.fromJson(reader, listType);

            if (loadedResources != null) {
                loadedResources.forEach(resource -> resources.put(resource.id(), resource));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during resource loading from: " + jsonFilePath, e);
        }
    }

    @Override
    public List<T> getAllResources() {
        return List.copyOf(resources.values());
    }

    @Override
    public T getResource(String id) {
        Objects.requireNonNull(id, "The resource ID must not be null.");

        T resource = resources.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found for this ID: " + id);
        }
        return resource;
    }
}