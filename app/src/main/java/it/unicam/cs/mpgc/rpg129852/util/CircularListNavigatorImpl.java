package it.unicam.cs.mpgc.rpg129852.util;

import java.util.List;

/**
 * Implementation of {@link CircularListNavigator} backed by a {@link List}.
 *
 * @param <T> the type of elements being navigated
 */
public class CircularListNavigatorImpl<T> implements CircularListNavigator<T> {

    private final List<T> list;
    private int currentIndex = 0;

    /**
     * Constructs a new circular navigator over the provided list.
     * The initial position is set to the first element (index 0).
     *
     * @param list the list of elements to navigate
     * @throws IllegalArgumentException if the list is null or empty
     */
    public CircularListNavigatorImpl(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("The provided list must not be null or empty.");
        }
        this.list = list;
    }

    @Override
    public T getCurrentElement() {
        return list.get(currentIndex);
    }

    @Override
    public void moveToPrevious() {
        currentIndex = (currentIndex - 1 + list.size()) % list.size();
    }

    @Override
    public void moveToNext() {
        currentIndex = (currentIndex + 1) % list.size();
    }
}