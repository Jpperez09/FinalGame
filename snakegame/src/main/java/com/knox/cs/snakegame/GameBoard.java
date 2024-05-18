package com.knox.cs.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameBoard extends Pane {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TILE_SIZE = 20;

    private Timeline timeline;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;
    private Button restartButton;

    public GameBoard() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: black;");

        snake = new Snake();
        food = new Food();
        score = 0;
        gameOver = false;

        // Initialize and configure the restart button
        restartButton = new Button("Restart");
        restartButton.setLayoutX(WIDTH / 2 - 40);
        restartButton.setLayoutY(HEIGHT / 2 - 20);
        restartButton.setVisible(false);
        restartButton.setOnAction(e -> restartGame());

        this.getChildren().add(restartButton);

        this.setFocusTraversable(true);
        this.requestFocus();

        this.setOnKeyPressed(event -> handleKeyPress(event));

        timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> run()));
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
            restartButton.setVisible(true); // Show the restart button
            return;
        }

        snake.move();

        if (snake.isFoodEaten(food)) {
            food.relocate(snake);
            score++;
        }

        draw();
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the grid
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(1);
        for (int x = 0; x < WIDTH; x += TILE_SIZE) {
            gc.strokeLine(x, 0, x, HEIGHT);
        }
        for (int y = 0; y < HEIGHT; y += TILE_SIZE) {
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Draw the border
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, WIDTH - 2, HEIGHT - 2);

        // Draw the snake
        gc.setFill(Color.GREEN);
        for (Point point : snake.getBody()) {
            gc.fillRect(point.getX() * TILE_SIZE, point.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        gc.setFill(Color.RED);
        Point foodPoint = food.getPoint();
        gc.fillRect(foodPoint.getX() * TILE_SIZE, foodPoint.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw the score
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 10, 10);

        // Draw game over message
        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.fillText("Game Over! Press Enter to Restart", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    private void restartGame() {
        snake = new Snake();
        food = new Food();
        score = 0;
        gameOver = false;
        restartButton.setVisible(false); // Hide the restart button
        timeline.play();
        this.requestFocus();
    }
}
