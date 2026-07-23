package it.unicam.cs.mpgc.rpg129852.asset;

import java.util.List;

public interface AssetRegistry<T extends Asset> {
    void loadAssets(String filePath);
    List<T> getAllAssets();
    T getAsset(String id);
}