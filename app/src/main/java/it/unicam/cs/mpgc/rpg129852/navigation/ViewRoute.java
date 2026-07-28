package it.unicam.cs.mpgc.rpg129852.navigation;

public enum ViewRoute {
    MAIN_MENU("/view/MainMenu.fxml"),
    DISCIPLE_CREATION("/view/DiscipleCreation.fxml"),
    LOAD_GAME("/view/LoadGame.fxml"),
    PLAYER_MENU("/view/PlayerMenu.fxml");

    private final String path;

    ViewRoute(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}