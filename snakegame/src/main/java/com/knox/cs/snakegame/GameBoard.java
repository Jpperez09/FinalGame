package com.knox.cs.snakegame;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    public GameBoard() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: black;");

        snake = new Snake();
        food = new Food();

        // Set the focus to the GameBoard
        this.setFocusTraversable(true);
        this.requestFocus();

        // Add key event handler
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
        // Ensure the GameBoard is focused
        this.requestFocus();
    }

    private void run() {
        if (!snake.isAlive()) {
            timeline.stop();
            System.out.println("Game Over");
            return;
        }

        snake.move();

        if (snake.isFoodEaten(food)) {
            food.relocate(snake);
        }

        draw();
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the snake
        gc.setFill(Color.GREEN);
        for (Point point : snake.getBody()) {
            gc.fillRect(point.getX() * TILE_SIZE, point.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        gc.setFill(Color.RED);
        Point foodPoint = food.getPoint();
        gc.fillRect(foodPoint.getX() * TILE_SIZE, foodPoint.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}