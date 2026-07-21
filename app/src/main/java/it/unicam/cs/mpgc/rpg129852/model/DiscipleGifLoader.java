package it.unicam.cs.mpgc.rpg129852.model;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class DiscipleGifLoader {

    private static final String GIF_PATH_TEMPLATE = "/gifs/%s-disciple.gif";

    public static List<Image> initializeGifs(String[] colors) {
        List<Image> images = new ArrayList<>();
        for (String color : colors) {
            images.add(loadSingleImage(color));
        }
        return images;
    }

    private static Image loadSingleImage(String color) {
        String path = String.format(GIF_PATH_TEMPLATE, color);
        URL resource = DiscipleGifLoader.class.getResource(path);

        if (resource == null)
            throw new IllegalArgumentException("Resource not found: " + path);

        return new Image(resource.toExternalForm());
    }
}
