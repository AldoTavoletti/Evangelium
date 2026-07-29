package it.unicam.cs.mpgc.rpg129852.asset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceRegistryImpl<T extends Resource> implements ResourceRegistry<T> {

    private final Map<String, T> resources = new HashMap<>();
    private final String jsonFilePath;
    private final Type listType;
    private final Gson gson;

    public ResourceRegistryImpl(String jsonFilePath, Class<T> typeParameterClass, Gson gson) {
        this.jsonFilePath = jsonFilePath;
        this.gson = gson;
        this.listType = TypeToken.getParameterized(List.class, typeParameterClass).getType();
    }

    @Override
    public void loadResources() {
        InputStream inputStream = getClass().getResourceAsStream(jsonFilePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File JSON not found in the classpath: " + jsonFilePath);
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
        T resource = resources.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found for this ID: " + id);
        }
        return resource;
    }
}