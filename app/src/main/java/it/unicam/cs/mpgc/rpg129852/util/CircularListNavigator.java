package it.unicam.cs.mpgc.rpg129852.util;

/**
 * Defines a contract for navigating a collection of elements in a circular manner.
 * Reaching the end of the collection and moving forward wraps around to the beginning,
 * and moving backward from the beginning wraps around to the end.
 *
 * @param <T> the type of elements being navigated
 */
public interface CircularListNavigator<T> {

    /**
     * Retrieves the element at the current navigation position.
     *
     * @return the current element of type {@code T}
     */
    T getCurrentElement();

    /**
     * Moves the navigation cursor to the previous element in the sequence.
     * If the cursor is at the first element, it wraps around to the last element.
     */
    void moveToPrevious();

    /**
     * Moves the navigation cursor to the next element in the sequence.
     * If the cursor is at the last element, it wraps around to the first element.
     */
    void moveToNext();
}