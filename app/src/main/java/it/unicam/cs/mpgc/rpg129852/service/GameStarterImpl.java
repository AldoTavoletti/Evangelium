package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.util.NameValidator;
import it.unicam.cs.mpgc.rpg129852.util.SaveNameGenerator;
import it.unicam.cs.mpgc.rpg129852.util.SaveNameManager;

import javax.xml.validation.Validator;
import java.util.List;

public class GameStarterImpl implements GameStarter {

    private final GameRepository repository;
    private final GameFactory gameFactory;
    private final SaveNameManager saveNameManager;

    public GameStarterImpl(GameRepository repository,
                           GameFactory gameFactory,
                           SaveNameManager saveNameManager) {
        this.repository = repository;
        this.gameFactory = gameFactory;
        this.saveNameManager = saveNameManager;
    }

    public void startNewGame(String discipleName, String discipleJob, String color, String saveName, boolean forceOverwrite) {

        String finalSaveName = saveNameManager.resolveAndValidate(saveName, forceOverwrite);

        DiscipleData discipleData = new DiscipleData(discipleName, discipleJob, color);
        GameState gameState = new GameState(discipleData);

        Game game = gameFactory.create(finalSaveName, gameState);

        repository.save(game);
    }

}
