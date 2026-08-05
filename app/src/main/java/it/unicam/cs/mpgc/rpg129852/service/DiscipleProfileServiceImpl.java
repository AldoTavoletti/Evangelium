package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

public class DiscipleProfileServiceImpl implements DiscipleProfileService {

    private static final String ERR_NO_SESSION = "Ci deve essere un contesto di gioco attivo per accedere al menu giocatore.";

    private final GameProvider gameProvider;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

    public DiscipleProfileServiceImpl(GameProvider gameProvider,
                                      ResourceRegistry<DiscipleAsset> discipleAssetRegistry) {
        this.gameProvider = gameProvider;
        this.discipleAssetRegistry = discipleAssetRegistry;
    }

    @Override
    public void requireActiveSession() {
        if (!gameProvider.hasActiveGame()) {
            throw new IllegalStateException(ERR_NO_SESSION);
        }
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