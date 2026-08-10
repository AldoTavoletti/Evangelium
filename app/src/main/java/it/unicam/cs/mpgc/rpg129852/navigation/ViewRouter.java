package it.unicam.cs.mpgc.rpg129852.navigation;

/**
 * Defines the contract for navigating between different views within the application.
 */
public interface ViewRouter {

    /**
     * Transitions the user interface to the specified target route.
     *
     * @param route the target destination to navigate to
     */
    void switchScene(ViewRoute route);
}
