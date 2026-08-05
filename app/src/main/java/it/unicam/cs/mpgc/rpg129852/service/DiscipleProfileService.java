package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;

public interface DiscipleProfileService {

    void requireActiveSession();

    DiscipleData getCurrentData();

    String getAvatarGifPath();
}