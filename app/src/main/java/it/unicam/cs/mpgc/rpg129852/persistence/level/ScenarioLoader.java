package it.unicam.cs.mpgc.rpg129852.persistence.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;

public interface ScenarioLoader {

    /**
     * Carica i dati di uno scenario a partire dal percorso del file fisico.
     *
     * @param scenarioPath Il percorso del file JSON contenente i dati dello scenario.
     * @return Un'istanza popolata di LevelScenario.
     */
    LevelScenario loadScenario(String scenarioPath);
}