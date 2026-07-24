package it.unicam.cs.mpgc.rpg129852.util;

public interface SaveNameManager {
    public String resolveAndValidate(String saveName, boolean forceOverwrite);
}
