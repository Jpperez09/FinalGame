package com.knox.cs.snakegame;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends VBox {
    private Button playButton;
    private Button configButton;
    private Stage primaryStage;
    private Text appleScore;
    private Text highestScore;
    private int score;
    private int highScore;

    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.score = 0; // Replace with actual score if available
        this.highScore = 0; // Replace with actual high score if available

        // Load the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/Fruits/Fondo.png"));
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        this.setBackground(new Background(bgImage));

        Image appleImage = new Image(getClass().getResourceAsStream("/Fruits/Apple_96x96.png"), 30, 30, true, true);
        ImageView appleImageView = new ImageView(appleImage);

        appleScore = new Text("0");
        appleScore.setFont(Font.font("Arial", 24));
        appleScore.setFill(Color.WHITE);

        VBox appleBox = new VBox(appleImageView, appleScore);
        appleBox.setAlignment(Pos.CENTER);

        Image trophyImage = new Image(getClass().getResourceAsStream("/Fruits/CROWN.png"), 30, 30, true, true);
        ImageView trophyImageView = new ImageView(trophyImage);

        highestScore = new Text("0");
        highestScore.setFont(Font.font("Arial", 24));
        highestScore.setFill(Color.WHITE);

        VBox trophyBox = new VBox(trophyImageView, highestScore);
        trophyBox.setAlignment(Pos.CENTER);

        playButton = new Button("Play");
        playButton.setPrefWidth(200);
        playButton.setFont(Font.font("Arial", 18));
        playButton.setOnAction(e -> startGame());

        configButton = new Button("Configuration");
        configButton.setPrefWidth(200);
        configButton.setFont(Font.font("Arial", 18));
        configButton.setOnAction(e -> showConfigurations());

        VBox buttonBox = new VBox(playButton, configButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        this.getChildren().addAll(appleBox, trophyBox, buttonBox);
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 20;");
    }

    private void startGame() {
        GameBoard gameBoard = new GameBoard(Config.getSpeed()); // Pass the selected speed
        Scene gameScene = new Scene(gameBoard, GameBoard.WIDTH, GameBoard.HEIGHT);
        primaryStage.setScene(gameScene);
        gameBoard.startGame();
    }

    private void showConfigurations() {
        VBox configBox = new VBox();
        configBox.setSpacing(10);
        configBox.setAlignment(Pos.CENTER);

        Text configTitle = new Text("Select Game Speed");
        configTitle.setFont(Font.font("Arial", 24));
        configTitle.setFill(Color.BLACK);

        ToggleGroup speedGroup = new ToggleGroup();

        ToggleButton fastButton = new ToggleButton("Fast");
        fastButton.setToggleGroup(speedGroup);
        fastButton.setUserData("Fast");

        ToggleButton normalButton = new ToggleButton("Normal");
        normalButton.setToggleGroup(speedGroup);
        normalButton.setUserData("Normal");
        normalButton.setSelected("Normal".equals(Config.getSpeed())); // Set selected based on saved speed

        ToggleButton slowButton = new ToggleButton("Slow");
        slowButton.setToggleGroup(speedGroup);
        slowButton.setUserData("Slow");
        slowButton.setSelected("Slow".equals(Config.getSpeed())); // Set selected based on saved speed

        speedGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Config.setSpeed(newValue.getUserData().toString());
            }
        });

        HBox speedBox = new HBox(10, fastButton, normalButton, slowButton);
        speedBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(200);
        saveButton.setFont(Font.font("Arial", 18));
        saveButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(new MainMenu(primaryStage), 600, 400)); // Keep main menu size 600x400
        });

        Button backButton = new Button("Back");
        backButton.setPrefWidth(200);
        backButton.setFont(Font.font("Arial", 18));
        backButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(new MainMenu(primaryStage), 600, 400)); // Keep main menu size 600x400
        });

        VBox buttonBox = new VBox(saveButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        configBox.getChildren().addAll(configTitle, speedBox, buttonBox);

        Scene configScene = new Scene(configBox, 600, 400); // Keep main menu size 600x400
        primaryStage.setScene(configScene);
    }
}
