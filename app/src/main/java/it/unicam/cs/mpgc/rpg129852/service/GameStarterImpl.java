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

    public void startNewGame(String discipleName, String discipleJob, String color, String saveName, boolean forceOverwrite) {
        DiscipleData discipleData = new DiscipleData(discipleName, discipleJob, color);
        GameState gameState = new GameState(discipleData);

        List<String> existingSaves = repository.getAvailableSaves();

        if (saveName == null || saveName.trim().isEmpty()) {
            saveName = generateDefaultSaveName(existingSaves);
        }

        // syntax validation
        nameValidator.validate(saveName);

        // semantic validation
        if (existingSaves.contains(saveName) && !forceOverwrite) {
            throw new IllegalStateException("A saving with the same name already exists.");
        }

        Game game = gameFactory.create(saveName, gameState);

        repository.save(game);
    }

    private String generateDefaultSaveName(List<String> existingSaves) {
        String baseName = "untitled";

        if (!existingSaves.contains(baseName)) {
            return baseName;
        }

        int counter = 1;
        String newName;
        do {
            newName = baseName + "(" + counter + ")";
            counter++;
        } while (existingSaves.contains(newName));

        return newName;
    }

}
