package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.service.LevelEngine;
import javafx.fxml.FXML;

public class GameplayController {

    private final LevelEngine levelEngine;
    private final GameSessionManager sessionManager;

    public GameplayController(LevelEngine levelEngine, GameSessionManager sessionManager) {
        this.levelEngine = levelEngine;
        this.sessionManager = sessionManager;
    }

    @FXML
    void initialize() {
    }

}
