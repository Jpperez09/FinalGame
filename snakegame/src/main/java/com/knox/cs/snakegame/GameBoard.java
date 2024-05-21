package com.knox.cs.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameBoard extends Pane {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    public static final int TILE_SIZE = 20;
    private static final int ANIMATION_STEPS = 10;

    private Timeline timeline;
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;
    private Button restartButton;
    private int animationStep;

    // Load images
    private Image headUp, headDown, headLeft, headRight;
    private Image bodyHorizontal, bodyVertical, bodyTopLeft, bodyTopRight, bodyBottomLeft, bodyBottomRight;
    private Image tailUp, tailDown, tailLeft, tailRight;
    private Image apple;

    public GameBoard() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: black;");

        // Load images and print paths
        headUp = loadImage("/Graphics/head_up.png");
        headDown = loadImage("/Graphics/head_down.png");
        headLeft = loadImage("/Graphics/head_left.png");
        headRight = loadImage("/Graphics/head_right.png");
        bodyHorizontal = loadImage("/Graphics/body_horizontal.png");
        bodyVertical = loadImage("/Graphics/body_vertical.png");
        bodyTopLeft = loadImage("/Graphics/body_topleft.png");
        bodyTopRight = loadImage("/Graphics/body_topright.png");
        bodyBottomLeft = loadImage("/Graphics/body_bottomleft.png");
        bodyBottomRight = loadImage("/Graphics/body_bottomright.png");
        tailUp = loadImage("/Graphics/tail_up.png");
        tailDown = loadImage("/Graphics/tail_down.png");
        tailLeft = loadImage("/Graphics/tail_left.png");
        tailRight = loadImage("/Graphics/tail_right.png");
        apple = loadImage("/Graphics/apple.png");

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

    private Image loadImage(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        if (image.isError()) {
            System.out.println("Error loading image: " + path);
        } else {
            System.out.println("Loaded image: " + path);
        }
        return image;
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
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point current = snake.getBody().get(i);
    
            Image image = null;
            if (i == 0) { // Head
                switch (snake.getDirection()) {
                    case UP:
                        image = headUp;
                        break;
                    case DOWN:
                        image = headDown;
                        break;
                    case LEFT:
                        image = headLeft;
                        break;
                    case RIGHT:
                        image = headRight;
                        break;
                }
            } else if (i == snake.getBody().size() - 1) { // Tail
                Point beforeTail = snake.getBody().get(i - 1);
                if (beforeTail.getY() < current.getY()) {
                    image = tailDown;
                } else if (beforeTail.getY() > current.getY()) {
                    image = tailUp;
                } else if (beforeTail.getX() < current.getX()) {
                    image = tailRight;
                } else if (beforeTail.getX() > current.getX()) {
                    image = tailLeft;
                }
            } else { // Body
                Point next = snake.getBody().get(i + 1);
                Point previous = snake.getBody().get(i - 1);
    
                if (previous.getX() == next.getX()) {
                    image = bodyVertical;
                } else if (previous.getY() == next.getY()) {
                    image = bodyHorizontal;
                } else if ((previous.getX() < current.getX() && next.getY() < current.getY()) ||
                           (next.getX() < current.getX() && previous.getY() < current.getY())) {
                    image = bodyTopRight;
                } else if ((previous.getX() < current.getX() && next.getY() > current.getY()) ||
                           (next.getX() < current.getX() && previous.getY() > current.getY())) {
                    image = bodyBottomRight;
                } else if ((previous.getX() > current.getX() && next.getY() < current.getY()) ||
                           (next.getX() > current.getX() && previous.getY() < current.getY())) {
                    image = bodyTopLeft;
                } else if ((previous.getX() > current.getX() && next.getY() > current.getY()) ||
                           (next.getX() > current.getX() && previous.getY() > current.getY())) {
                    image = bodyBottomLeft;
                }
            }
    
            if (image != null) {
                gc.drawImage(image, current.getX() * TILE_SIZE, current.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    
        // Draw the food
        food.draw(gc);
    
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
