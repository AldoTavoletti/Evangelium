package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

/**
 * A record used to pass the data chosen by the user to the authoritative services.
 *
 * @param discipleName the name of the disciple
 * @param job the job of the disciple
 * @param color the color of the chosen disciple GIF, which acts as an ID
 * @param saveName the name for the save file
 */
public record NewGameRequest(String discipleName,
                             Job job,
                             String color,
                             String saveName) {
}