package com.knox.cs.snakegame;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        GameBoard gameBoard = new GameBoard();
        Scene scene = new Scene(gameBoard, 600, 400);
        
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        gameBoard.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}