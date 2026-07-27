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
import java.util.Objects;

public class DiscipleAssetRegistry implements AssetRegistry<DiscipleAsset> {

    private final Map<String, DiscipleAsset> assets = new HashMap<>();

    public void loadAssets(String jsonFilePath) {
        InputStream inputStream = getClass().getResourceAsStream(jsonFilePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File JSON not found in the classpath: " + jsonFilePath);
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            // java uses type erasure, so we need to save the type
            Type listType = new TypeToken<List<DiscipleAsset>>() {}.getType();
            List<DiscipleAsset> loadedAssets = new Gson().fromJson(reader, listType);

            loadedAssets.forEach(asset -> assets.put(asset.id(), asset));

        } catch (Exception e) {
            throw new RuntimeException("Error during asset loading from: " + jsonFilePath, e);
        }
    }

    public List<DiscipleAsset> getAllAssets() {
        return List.copyOf(assets.values());
    }

    public DiscipleAsset getAsset(String id) {
        DiscipleAsset asset = assets.get(id);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found for this ID: " + id);
        }
        return asset;
    }
}