package it.unicam.cs.mpgc.rpg129852.model;

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
}
