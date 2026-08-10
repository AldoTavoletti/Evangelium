package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.ScriptureResource;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.Objects;

/**
 * Concrete implementation of the {@link ScriptureCatalog} interface.
 * It uses a {@link ResourceRegistry} to access the underlying scripture data.
 */
public class ScriptureCatalogImpl implements ScriptureCatalog {

    private final ResourceRegistry<ScriptureResource> registry;

    /**
     * Constructs a new catalog using the specified resource registry.
     *
     * @param registry  the data source containing the book entities
     * @throws NullPointerException if the provided registry is null
     */
    public ScriptureCatalogImpl(ResourceRegistry<ScriptureResource> registry) {
        this.registry = Objects.requireNonNull(registry, "The resource registry must not be null.");
    }


    @Override
    public String getScriptureText(String scriptureId) {
        Objects.requireNonNull(scriptureId, "The scripture ID must not be null.");
        return registry.getResource(scriptureId).text();
    }
}
