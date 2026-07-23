package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.util.NameValidator;

import javax.xml.validation.Validator;
import java.util.List;

public class GameStarterImpl implements GameStarter {

    private GameRepository repository;
    private GameFactory gameFactory;
    private NameValidator nameValidator;

    public GameStarterImpl(GameRepository repository, GameFactory gameFactory, NameValidator nameValidator) {
        this.repository = repository;
        this.gameFactory = gameFactory;
        this.nameValidator = nameValidator;
    }

    public void startNewGame(String discipleName, String discipleJob, String color, String saveName) {
        DiscipleData discipleData = new DiscipleData(discipleName, discipleJob, color);
        GameState gameState = new GameState(discipleData);

        // syntax validation
        nameValidator.validate(saveName);

        // semantic validation
        List<String> existingSaves = repository.getAvailableSaves();

        if (existingSaves.contains(saveName)) {
            throw new IllegalStateException("A saving with the same name already exists.");
        }

        Game game = gameFactory.create(saveName, gameState);

        repository.save(game);
    }

}
