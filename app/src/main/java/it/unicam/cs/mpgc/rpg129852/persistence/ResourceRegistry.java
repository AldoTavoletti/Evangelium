package it.unicam.cs.mpgc.rpg129852.persistence;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;

import java.util.List;

public interface ResourceRegistry<T extends Resource> {
    void loadResources();
    List<T> getAllResources();
    T getResource(String id);
}