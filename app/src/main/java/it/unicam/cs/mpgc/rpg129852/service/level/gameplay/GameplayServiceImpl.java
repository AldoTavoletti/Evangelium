package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.context.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link GameplayService}.
 * It acts as an orchestrator, delegating specific domain tasks (saving, calculating rewards,
 * managing phase sequences) to specialized injected components, while maintaining the state
 * of the current active gameplay session.
 */
public class GameplayServiceImpl implements GameplayService {

    private final LevelProvider levelProvider;
    private final LevelSaver levelSaver;
    private final LevelRewardsCalculator rewardsCalculator;
    private final LevelEngine levelEngine;

    private final List<PhaseAnswer> phaseAnswers;
    private double currentProblemValue;

    /**
     * Constructs a new gameplay service session.
     *
     * @param levelProvider     provides access to the static data of the current level
     * @param levelSaver        handles the persistence of the level outcome
     * @param rewardsCalculator calculates the final score based on the player's choices
     * @param levelEngine       manages the sequential progression of level phases
     * @throws NullPointerException if any of the dependencies are null
     */
    public GameplayServiceImpl(LevelProvider levelProvider,
                               LevelSaver levelSaver,
                               LevelRewardsCalculator rewardsCalculator,
                               LevelEngine levelEngine) {
        this.levelProvider = Objects.requireNonNull(levelProvider, "The level provider must not be null.");
        this.levelSaver = Objects.requireNonNull(levelSaver, "The level saver must not be null.");
        this.rewardsCalculator = Objects.requireNonNull(rewardsCalculator, "The rewards calculator must not be null.");
        this.levelEngine = Objects.requireNonNull(levelEngine, "The level engine must not be null.");

        this.phaseAnswers = new ArrayList<>();
        this.currentProblemValue = getMaxProblemValue();
    }

    @Override
    public void completeLevel() {
        Virtues maxRewards = levelProvider.getCurrentLevel().metadata().maxRewards();
        String currentLevelId = levelProvider.getCurrentLevel().metadata().id();

        Score score = rewardsCalculator.calculate(maxRewards, phaseAnswers);

        levelSaver.save(currentLevelId, score);
    }

    @Override
    public void submitAnswer(PhaseAnswer answer) {
        Objects.requireNonNull(answer, "The submitted answer must not be null.");

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
    public String getNpcName() {
        return levelProvider.getCurrentLevel().scenario().npc().name();
    }

    @Override
    public ProblemType getProblemType() {
        return levelProvider.getCurrentLevel().scenario().npc().problemType();
    }
}