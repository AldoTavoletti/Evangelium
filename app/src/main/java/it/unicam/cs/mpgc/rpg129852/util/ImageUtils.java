package it.unicam.cs.mpgc.rpg129852.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Objects;

/**
 * Utility class for managing and loading image resources.
 * This class cannot be instantiated.
 */
public class ImageUtils {

    // private constructor to prevent instantiation
    private ImageUtils() {
    }

    /**
     * Loads a JavaFX {@link Image} from the specified resource path.
     *
     * @param path the internal resource path of the image (e.g., "/images/icon.png")
     * @return the loaded JavaFX Image
     * @throws IllegalArgumentException if the path is null or the resource cannot be found
     * @throws RuntimeException if the image stream cannot be parsed correctly
     */
    public static Image loadImage(String path) {
        Objects.requireNonNull(path, "The image path must not be null.");

        InputStream imageStream = ImageUtils.class.getResourceAsStream(path);

        if (imageStream == null) {
            throw new IllegalArgumentException("Image resource not found at path: " + path);
        }

        try {
            return new Image(imageStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse the image located at: " + path, e);
        }
    }
}