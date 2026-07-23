package it.unicam.cs.mpgc.rpg129852.persistence;

import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

import java.util.List;

public interface GameRepository {

    void save(Game game);

    GameState load(String saveFileName);

    List<String> getAvailableSaves();

}
