package it.unicam.cs.mpgc.rpg129852.service.disciple;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

public class DiscipleProfileServiceImpl implements DiscipleProfileService {

    private final GameProvider gameProvider;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

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
    public String getAvatarGifPath() {
        DiscipleData data = getCurrentData();
        DiscipleAsset asset = discipleAssetRegistry.getResource(data.getColor());
        return asset.gifPath();
    }
}