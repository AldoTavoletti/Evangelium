package it.unicam.cs.mpgc.rpg129852.persistence;

import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;

import java.util.List;

public interface GameRepository extends AvailableSavesProvider {

    void save(Game game);

    Game load(String saveName);

    void delete(String saveName);

}
