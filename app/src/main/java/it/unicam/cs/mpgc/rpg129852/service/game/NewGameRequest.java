package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

public record NewGameRequest(String discipleName,
                             Job job,
                             String color,
                             String saveName) {
}