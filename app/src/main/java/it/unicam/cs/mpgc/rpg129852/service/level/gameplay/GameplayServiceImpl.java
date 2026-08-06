package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.context.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.ArrayList;
import java.util.List;

public class GameplayServiceImpl implements GameplayService {

    private final LevelProvider levelProvider;
    private final LevelSaver levelSaver;
    private final LevelRewardsCalculator rewardsCalculator;
    private final LevelEngine levelEngine;

    private final List<PhaseAnswer> phaseAnswers;
    private double currentProblemValue;

    public GameplayServiceImpl(LevelProvider levelProvider,
                               LevelSaver levelSaver,
                               LevelRewardsCalculator rewardsCalculator,
                               LevelEngine levelEngine) {
        this.levelProvider = levelProvider;
        this.levelSaver = levelSaver;
        this.rewardsCalculator = rewardsCalculator;
        this.levelEngine = levelEngine;
        this.phaseAnswers = new ArrayList<>();

        this.currentProblemValue = getMaxProblemValue();
    }

    @Override
    public void completeLevel() {

        Virtues maxRewards = levelProvider.getCurrentLevel().metadata().maxRewards();
        String currentLevelId = levelProvider.getCurrentLevel().metadata().id();

        Virtues obtainedRewards = rewardsCalculator.calculate(maxRewards, phaseAnswers);

        levelSaver.save(currentLevelId, obtainedRewards);
    }

    @Override
    public void submitAnswer(PhaseAnswer answer) {
        phaseAnswers.add(answer);

        double newProgress = this.currentProblemValue - answer.getHealValue();
        this.currentProblemValue = Math.round(newProgress * 100.0) / 100.0;
    }

    @Override
    public boolean isLevelWon() {
        return this.currentProblemValue <= 0.0;
    }

    @Override
    public double getCurrentProblemValue() {
        return this.currentProblemValue;
    }

    @Override
    public double getMaxProblemValue() {
        return levelProvider.getCurrentLevel().scenario().npc().maxProblemValue();
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
    public String getNpcImagePath() {
        return levelProvider.getCurrentLevel().scenario().npc().imagePath();
    }

    @Override
    public ProblemType getProblemType() {
        return levelProvider.getCurrentLevel().scenario().npc().problemType();
    }

}