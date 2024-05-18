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
    private static final int ANIMATION_STEPS = 5;

    private Timeline timeline;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;
    private Button restartButton;
    private int animationStep;

    public GameBoard() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: black;");

        snake = new Snake();
        food = new Food();
        score = 0;
        gameOver = false;
        animationStep = 0;

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

        timeline = new Timeline(new KeyFrame(Duration.millis(150 / ANIMATION_STEPS), e -> run()));
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

        if (animationStep == 0) {
            snake.move();
            if (snake.isFoodEaten(food)) {
                food.relocate(snake);
                score++;
            }
        }

        draw();

        animationStep = (animationStep + 1) % ANIMATION_STEPS;
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the checkerboard grid
        Color lightGreen = Color.web("#A2D149");
        Color darkGreen = Color.web("#AAD751");
        for (int x = 0; x < WIDTH; x += TILE_SIZE) {
            for (int y = 0; y < HEIGHT; y += TILE_SIZE) {
                if ((x / TILE_SIZE + y / TILE_SIZE) % 2 == 0) {
                    gc.setFill(lightGreen);
                } else {
                    gc.setFill(darkGreen);
                }
                gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw the border
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, WIDTH - 2, HEIGHT - 2);

        
        // Draw the snake
        Point previous = null;
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point point = snake.getBody().get(i);
            double x = point.getX() * TILE_SIZE;
            double y = point.getY() * TILE_SIZE;

            if (previous != null) {
                x = interpolate(previous.getX() * TILE_SIZE, x);
                y = interpolate(previous.getY() * TILE_SIZE, y);
            }

            if (i == 0) { // Head of the snake
                gc.setFill(Color.BLUE);
                gc.fillOval(x, y, TILE_SIZE, TILE_SIZE);

                // Draw eyes
                gc.setFill(Color.WHITE);
                double eyeSize = TILE_SIZE / 5.0;
                double eyeOffset = TILE_SIZE / 3.0;
                switch (snake.getDirection()) {
                    case UP:
                        gc.fillOval(x + eyeOffset, y + eyeOffset, eyeSize, eyeSize);
                        gc.fillOval(x + 2 * eyeOffset - eyeSize, y + eyeOffset, eyeSize, eyeSize);
                        break;
                    case DOWN:
                        gc.fillOval(x + eyeOffset, y + 2 * eyeOffset - eyeSize, eyeSize, eyeSize);
                        gc.fillOval(x + 2 * eyeOffset - eyeSize, y + 2 * eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                    case LEFT:
                        gc.fillOval(x + eyeOffset, y + eyeOffset, eyeSize, eyeSize);
                        gc.fillOval(x + eyeOffset, y + 2 * eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                    case RIGHT:
                        gc.fillOval(x + 2 * eyeOffset - eyeSize, y + eyeOffset, eyeSize, eyeSize);
                        gc.fillOval(x + 2 * eyeOffset - eyeSize, y + 2 * eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                }
            } else {
                gc.setFill(Color.GREEN);
                gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
            previous = point;
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

    private double interpolate(double start, double end) {
        return start + ((end - start) * animationStep) / ANIMATION_STEPS;
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
