package it.unicam.cs.mpgc.rpg129852.service.disciple;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;

public interface DiscipleProfileService {

    void requireActiveSession();

    DiscipleData getCurrentData();

    String getAvatarGifPath();
}