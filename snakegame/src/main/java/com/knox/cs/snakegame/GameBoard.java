package com.knox.cs.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameBoard extends Pane {
    public static final int TILE_SIZE = 20;
    public static final int WIDTH = TILE_SIZE * 17;
    public static final int HEIGHT = TILE_SIZE * 15 + 40;  // Additional space for the score

    private static int highScore = 0;  // Static high score variable

    private Timeline timeline;
    private Renderer renderer;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;
    private Button restartButton;
    private int animationStep;
    private Text scoreText;
    private Text highScoreText;

    public GameBoard(String speed) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: black;");

        // Prevent stretching in full screen
        this.widthProperty().addListener((obs, oldVal, newVal) -> canvas.setWidth(WIDTH));
        this.heightProperty().addListener((obs, oldVal, newVal) -> canvas.setHeight(HEIGHT));

        renderer = new Renderer();

        snake = new Snake();
        
        food = new Food(snake);  // Pass the snake object to the Food constructor

        score = 0;
        gameOver = false;
        animationStep = 0;

        restartButton = new Button("Restart");
        restartButton.setLayoutX(WIDTH / 2 - 40);
        restartButton.setLayoutY(HEIGHT / 2 - 20);
        restartButton.setVisible(false);
        restartButton.setOnAction(e -> restartGame());

        this.getChildren().add(restartButton);

        scoreText = new Text("Score: 0");
        scoreText.setFill(javafx.scene.paint.Color.WHITE);

        highScoreText = new Text("High Score: 0");
        highScoreText.setFill(javafx.scene.paint.Color.WHITE);

        HBox scoreBox = new HBox(10);  // HBox with spacing of 10
        scoreBox.getChildren().addAll(scoreText, highScoreText);
        scoreBox.setLayoutX(10);
        scoreBox.setLayoutY(10);  // Position within the border

        this.getChildren().add(scoreBox);

        this.setFocusTraversable(true);
        this.requestFocus();

        this.setOnKeyPressed(event -> handleKeyPress(event));

        int speedDuration;
        switch (speed) {
            case "Fast":
                speedDuration = 10;
                break;
            case "Slow":
                speedDuration = 22;
                break;
            case "Normal":
            default:
                speedDuration = 16;
                break;
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(speedDuration), e -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        switch (key) {
            case UP:
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
                break;
            case DOWN:
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
                break;
            case LEFT:
                if (snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
                break;
            default:
                break;
        }
    }

    public void startGame() {
        timeline.play();
        this.requestFocus();
    }

    private void run() {
        if (!snake.isAlive()) {
            gameOver = true;
            timeline.stop();
            System.out.println("Game Over");
            restartButton.setVisible(true);
            if (score > highScore) {
                highScore = score;  // Update high score if the current score is higher
            }
            return;
        }

        if (animationStep == 0) {
            snake.move();
            if (snake.isFoodEaten(food)) {
                food.relocate(snake);
                score++;
                scoreText.setText("Score: " + score);  // Update score text
            }
        }

        renderer.setAnimationStep(animationStep);
        renderer.draw(gc, snake, food, gameOver, WIDTH, HEIGHT);  // Remove score from draw method

        animationStep = (animationStep + 1) % 10;
    }

    private void restartGame() {
        snake = new Snake();
        food = new Food(snake);  // Pass the snake object to the Food constructor
        score = 0;
        scoreText.setText("Score: 0");  // Reset score text
        highScoreText.setText("High Score: " + highScore);  // Display the current high score
        gameOver = false;
        restartButton.setVisible(false);
        timeline.play();
        this.requestFocus();
    }
}
