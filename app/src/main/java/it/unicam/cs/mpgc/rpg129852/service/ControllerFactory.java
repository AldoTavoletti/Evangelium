package it.unicam.cs.mpgc.rpg129852.service;

import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private final Map<Class<?>, Callable<Object>> registry = new HashMap<>();

    public void register(Class<?> type, Callable<Object> supplier) {
        registry.put(type, supplier);
    }

    @Override
    public Object call(Class<?> type) {
        try {
            if (registry.containsKey(type)) {
                return registry.get(type).call();
            }
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Critical error while creating the controller: " + type.getName(), e);
        }
    }
}