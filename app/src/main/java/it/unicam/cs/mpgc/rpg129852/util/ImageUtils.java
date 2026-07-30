package it.unicam.cs.mpgc.rpg129852.util;

import javafx.scene.image.Image;

public class ImageUtils {

    private ImageUtils() {
    }

    public static Image loadImage(String path) {
        try {
            return new Image(ImageUtils.class.getResourceAsStream(path));
        } catch (Exception e) {
            throw new RuntimeException("Impossibile caricare l'immagine al percorso: " + path, e);
        }
    }
}