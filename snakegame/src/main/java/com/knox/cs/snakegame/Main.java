package com.knox.cs.snakegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = new Scene(mainMenu, GameBoard.WIDTH, GameBoard.HEIGHT);

        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
