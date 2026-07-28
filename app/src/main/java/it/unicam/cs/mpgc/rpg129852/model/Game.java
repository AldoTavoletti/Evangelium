package it.unicam.cs.mpgc.rpg129852.model;

import java.util.Objects;

public class Game {

    private String saveName;
    private GameState  gameState;

    public Game(String saveName, GameState gameState){
        this.saveName = saveName;
        this.gameState = gameState;
    }

    public String getSaveName(){
        return saveName;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(saveName, game.saveName) && Objects.equals(gameState, game.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saveName, gameState);
    }
}
