package it.unicam.cs.mpgc.rpg129852;

import it.unicam.cs.mpgc.rpg129852.bootstrap.AppAssembler;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppAssembler assembler = new AppAssembler();
        assembler.assembleAndRun(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}