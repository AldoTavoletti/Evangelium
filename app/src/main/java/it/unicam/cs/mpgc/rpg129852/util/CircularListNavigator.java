package it.unicam.cs.mpgc.rpg129852.util;

public interface CircularListNavigator<T> {
    public T getCurrentElement();

    public void moveToPrevious();

    public void moveToNext();
}
