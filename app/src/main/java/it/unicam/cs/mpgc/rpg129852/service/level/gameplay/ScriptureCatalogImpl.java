package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.ScriptureResource;
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
