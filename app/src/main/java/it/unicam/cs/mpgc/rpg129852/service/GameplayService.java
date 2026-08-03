package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.UserPerformanceDetails;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public class GameplayService {

    private final LevelSessionManager levelSessionManager;
    private final LevelSaver levelSaver;
    private final LevelRewardsCalculator rewardsCalculator;
    private final LevelEngine levelEngine;

    public GameplayService(LevelSessionManager levelSessionManager,
                           LevelSaver levelSaver,
                           LevelRewardsCalculator rewardsCalculator,
                           LevelEngine levelEngine) {
        this.levelSessionManager = levelSessionManager;
        this.levelSaver = levelSaver;
        this.rewardsCalculator = rewardsCalculator;
        this.levelEngine = levelEngine;
    }

    public LevelEngine getEngine() {
        return levelEngine;
    }

    public LevelSessionManager getSessionManager() {
        return levelSessionManager;
    }

    public void completeLevel(double remainingProblem) {
        UserPerformanceDetails details = new UserPerformanceDetails(
                remainingProblem,
                levelEngine.getCurrentPhaseNumber()
        );

        Virtues obtainedRewards = rewardsCalculator.calculate(
                levelSessionManager.getMaxRewards(),
                levelEngine.getTotalNumberOfPhases(),
                details
        );

        levelSaver.save(levelSessionManager.getCurrentLevelId(), obtainedRewards);
    }
}