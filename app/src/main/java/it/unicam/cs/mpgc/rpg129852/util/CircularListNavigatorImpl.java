package it.unicam.cs.mpgc.rpg129852.util;

import java.util.List;

public class CircularListNavigatorImpl<T> implements CircularListNavigator {

    private final List<T> list;
    private int currentIndex = 0;

    public CircularListNavigatorImpl(List<T> list) {
        this.list = list;
    }

    public T getCurrentElement() {
        return list.get(currentIndex);
    }

    public void moveToPrevious() {
        currentIndex = (currentIndex + 1) % list.size();
    }

    public void moveToNext() {
        currentIndex = (currentIndex + list.size() - 1) % list.size();
    }

}
