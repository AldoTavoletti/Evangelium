package it.unicam.cs.mpgc.rpg129852.model;

import javafx.scene.image.Image;

import java.util.List;

public class CircularImageNavigator {

    private final List<Image> images;

    private int currentIndex = 0;

    public CircularImageNavigator(List<Image> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("The image list cannot be empty");
        }
        this.images = List.copyOf(images);
    }

    public void moveToNext() {
        currentIndex = (currentIndex + 1) % images.size();
    }

    public void moveToPrevious() {
        currentIndex = (currentIndex - 1 + images.size()) % images.size();
    }
    public Image getCurrentImage() {
        return images.get(currentIndex);
    }

}
