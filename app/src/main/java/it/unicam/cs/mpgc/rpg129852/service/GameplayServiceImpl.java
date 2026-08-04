package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.UserPerformanceDetails;
import it.unicam.cs.mpgc.rpg129852.model.ProblemType;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;

public class GameplayServiceImpl implements GameplayService {

    private final LevelSessionManager levelSessionManager;
    private final LevelSaver levelSaver;
    private final LevelRewardsCalculator rewardsCalculator;
    private final LevelEngine levelEngine;

    public GameplayServiceImpl(LevelSessionManager levelSessionManager,
                               LevelSaver levelSaver,
                               LevelRewardsCalculator rewardsCalculator,
                               LevelEngine levelEngine) {
        this.levelSessionManager = levelSessionManager;
        this.levelSaver = levelSaver;
        this.rewardsCalculator = rewardsCalculator;
        this.levelEngine = levelEngine;
    }

    @Override
    public boolean hasNextPhase() {
        return levelEngine.hasNextPhase();
    }

    @Override
    public LevelPhase getNextPhase() {
        return levelEngine.getNextPhase();
    }

    @Override
    public int getCurrentPhaseNumber() {
        return levelEngine.getCurrentPhaseNumber();
    }

    @Override
    public int getTotalNumberOfPhases() {
        return levelEngine.getTotalNumberOfPhases();
    }

    @Override
    public double getMaxProblemValue() {
        return levelSessionManager.getCurrentLevel().scenario().npc().maxProblemValue();
    }

    @Override
    public String getNpcImagePath() {
        return levelSessionManager.getCurrentLevel().scenario().npc().imagePath();
    }

    public ProblemType getProblemType() {
        System.out.println(levelSessionManager.getCurrentLevel().scenario().npc());
        return levelSessionManager.getCurrentLevel().scenario().npc().problemType();
    }

    @Override
    public void completeLevel(double remainingProblem) {
        int currentPhase = levelEngine.getCurrentPhaseNumber();
        int totalPhases = levelEngine.getTotalNumberOfPhases();
        Virtues maxRewards = levelSessionManager.getCurrentLevel().metadata().maxRewards();
        String currentLevelId = levelSessionManager.getCurrentLevel().metadata().id();

        UserPerformanceDetails details = new UserPerformanceDetails(remainingProblem, currentPhase);

        Virtues obtainedRewards = rewardsCalculator.calculate(maxRewards, totalPhases, details);

        levelSaver.save(currentLevelId, obtainedRewards);
    }
}