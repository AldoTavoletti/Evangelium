package it.unicam.cs.mpgc.rpg129852.service.disciple;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;

/**
 * Defines the contract for accessing the disciple data and its avatar GIF path.
 */
public interface DiscipleProfileService {

    /**
     * Retrieves the disciple data from the current game context.
     *
     * @return the current context's disciple data
     */
    DiscipleData getCurrentData();

    /**
     * Retrieves the GIF path of the current context's disciple.
     *
     * @return the GIF path for the current disciple
     */
    String getGifPath();
}