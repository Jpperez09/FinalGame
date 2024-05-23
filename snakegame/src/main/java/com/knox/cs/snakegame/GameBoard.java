package com.knox.cs.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameBoard extends Pane {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    public static final int TILE_SIZE = 20;
    private static final int ANIMATION_STEPS = 10;

    private Timeline timeline;
    private Renderer renderer;
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

        // Prevent stretching in full screen
        this.widthProperty().addListener((obs, oldVal, newVal) -> canvas.setWidth(WIDTH));
        this.heightProperty().addListener((obs, oldVal, newVal) -> canvas.setHeight(HEIGHT));

        renderer = new Renderer();

        snake = new Snake();
        food = new Food();
        score = 0;
        gameOver = false;
        animationStep = 0;

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
            restartButton.setVisible(true);
            return;
        }

        if (animationStep == 0) {
            snake.move();
            if (snake.isFoodEaten(food)) {
                food.relocate(snake);
                score++;
            }
        }

        renderer.setAnimationStep(animationStep);
        renderer.draw(gc, snake, food, score, gameOver, WIDTH, HEIGHT);

        animationStep = (animationStep + 1) % ANIMATION_STEPS;
    }

    private void restartGame() {
        snake = new Snake();
        food = new Food();
        score = 0;
        gameOver = false;
        restartButton.setVisible(false);
        timeline.play();
        this.requestFocus();
    }
}
