package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.ScriptureResource;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

public class ScriptureCatalogImpl implements ScriptureCatalog {

    private final ResourceRegistry<ScriptureResource> registry;

    public ScriptureCatalogImpl(ResourceRegistry<ScriptureResource> registry) {
        this.registry = registry;
    }


    @Override
    public String getScriptureText(String scriptureId) {
        return registry.getResource(scriptureId).text();
    }
}
