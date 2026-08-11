package it.unicam.cs.mpgc.rpg129852.bootstrap;

import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * A custom factory for instantiating JavaFX controllers.
 * It acts as a lightweight Dependency Injection container, allowing controllers
 * to be created with complex dependencies rather than relying solely on default no-argument constructors.
 */
public class ControllerFactory implements Callback<Class<?>, Object> {

    private final Map<Class<?>, Callable<Object>> registry = new HashMap<>();

    /**
     * Registers a supplier function for a specific controller type.
     *
     * @param type     the class representing the controller type
     * @param supplier the logic defining how to instantiate the controller
     * @throws NullPointerException if either the type or the supplier is null
     */
    public void register(Class<?> type, Callable<Object> supplier) {
        Objects.requireNonNull(type, "The controller type must not be null.");
        Objects.requireNonNull(supplier, "The controller supplier must not be null.");

        registry.put(type, supplier);
    }

    /**
     * Creates an instance of the requested controller type.
     * If a specific supplier is registered for the type, it is invoked.
     * Otherwise, it falls back to instantiating the controller using its default no-argument constructor.
     *
     * @param type the class of the controller to instantiate
     * @return an instance of the requested controller
     * @throws RuntimeException if the instantiation fails (e.g., missing default constructor or exception in supplier)
     */
    @Override
    public Object call(Class<?> type) {
        Objects.requireNonNull(type, "The requested controller type must not be null.");

        try {
            Callable<Object> supplier = registry.get(type);

            if (supplier != null) {
                return supplier.call();
            }

            // Fallback strategy for controllers without dependencies
            return type.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Critical error while creating the controller: " + type.getName(), e);
        }
    }
}