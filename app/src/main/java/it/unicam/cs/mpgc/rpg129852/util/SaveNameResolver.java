package it.unicam.cs.mpgc.rpg129852.util;

public interface SaveNameResolver {
    public String resolveFinalName(String proposedName, boolean forceOverwrite);
}
