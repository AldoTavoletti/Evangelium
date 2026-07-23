package it.unicam.cs.mpgc.rpg129852.asset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiscipleAssetRegistry implements AssetRegistry<DiscipleAsset> {

    private final Map<String, DiscipleAsset> assets = new HashMap<>();

    public void loadAssets(String jsonFilePath) {
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(jsonFilePath)))) {

            // to avoid type erasure
            Type listType = new TypeToken<List<DiscipleAsset>>(){}.getType();

            List<DiscipleAsset> loadedAssets = gson.fromJson(reader, listType);

            for (DiscipleAsset asset : loadedAssets) {
                assets.put(asset.id(), asset);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during disciples' assets loading", e);
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