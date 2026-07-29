package it.unicam.cs.mpgc.rpg129852.asset;

import java.util.List;

public interface ResourceRegistry<T extends Resource> {
    void loadResources();
    List<T> getAllResources();
    T getResource(String id);
}