package it.unicam.cs.mpgc.rpg129852.model;

public class Game {

    private String savePath;
    private GameState  gameState;

    public Game(String savePath, GameState gameState){
        this.savePath = savePath;
        this.gameState = gameState;
    }

    public String getSavePath(){
        return savePath;
    }

    public GameState getGameState() {
        return gameState;
    }
}
