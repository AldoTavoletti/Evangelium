package it.unicam.cs.mpgc.rpg129852.service.disciple;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

/**
 * Concrete implementation of the {@link DiscipleProfileService} interface.
 * It uses a {@link GameProvider} to obtain the disciple data and a {@link ResourceRegistry} to obtain the disciple's gif path
 */
public class DiscipleProfileServiceImpl implements DiscipleProfileService {

    private final GameProvider gameProvider;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

    /**
     * Constructs a new disciple profile service using the specified game provider and resource registry.
     * @param gameProvider  the context from which the disciple data is retrieved
     * @param discipleAssetRegistry the registry from which the disciple's GIF path is retrieved
     */
    public DiscipleProfileServiceImpl(GameProvider gameProvider,
                                      ResourceRegistry<DiscipleAsset> discipleAssetRegistry) {
        this.gameProvider = gameProvider;
        this.discipleAssetRegistry = discipleAssetRegistry;
    }

    @Override
    public DiscipleData getCurrentData() {
        return gameProvider.getCurrentGame().gameState().getDiscipleData();
    }

    @Override
    public String getGifPath() {
        DiscipleData data = getCurrentData();
        DiscipleAsset asset = discipleAssetRegistry.getResource(data.getColor());
        return asset.gifPath();
    }
}