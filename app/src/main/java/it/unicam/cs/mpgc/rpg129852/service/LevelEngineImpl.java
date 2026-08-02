package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class LevelEngineImpl implements LevelEngine {

    private final LevelData levelData;
    private int phaseIndex = 0;
    private GameSessionManager gameSessionManager;
    private GameRepository repository;

    public LevelEngineImpl(LevelData levelData, GameSessionManager gameSessionManager, GameRepository repository) {
        this.levelData = levelData;
        this.gameSessionManager = gameSessionManager;
        this.repository = repository;
    }

    public void endLevel(double problemValue) {
        Virtues virtueRewards = getVirtueRewards(problemValue);
        Game currentGame = gameSessionManager.getCurrentGame();
        GameState currentGameState = currentGame.getGameState();

        currentGameState.recordLevelScore(levelData.getMetadata().id(), virtueRewards);

        repository.save(currentGame);
    }

    private Virtues getVirtueRewards(double problemValue) {
        if (problemValue > 0.0)
            return new Virtues(0, 0, 0);

        return switch (phaseIndex) {
            case 2 -> getMaxRewards();
            case 3 -> getHalfMaxRewards();
            default -> throw new RuntimeException();
        };
    }

    private Virtues getHalfMaxRewards() {
        Virtues maxRewards = getMaxRewards();

        int faith = maxRewards.faith() / 2;
        int hope = maxRewards.hope() / 2;
        int love = maxRewards.love() / 2;

        return new Virtues(faith, hope, love);
    }

    private Virtues getMaxRewards() {
        return levelData.getMetadata().maxRewards();
    }

    public String getNpcImagePath() {
        return levelData.getScenario().npc().imagePath();
    }

    public boolean hasNextPhase() {
        return phaseIndex < levelData.getScenario().phases().length;
    }

    public LevelPhase getNextPhase(){

        LevelPhase nextPhase = levelData.getScenario().phases()[phaseIndex];

        phaseIndex++;

        return nextPhase;
    }

    public double getMaxProblemValue(){
        return levelData.getScenario().npc().maxProblemValue();
    }

}