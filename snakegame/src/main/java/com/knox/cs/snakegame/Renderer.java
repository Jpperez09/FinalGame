package com.knox.cs.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Renderer {
    private static final int TILE_SIZE = 20;
    private static final int ANIMATION_STEPS = 10;

    private int animationStep;

    private Image headUp, headDown, headLeft, headRight;
    private Image apple;

    public Renderer() {
        // Load images
        headUp = loadImage("/Graphics/head_up.png");
        headDown = loadImage("/Graphics/head_down.png");
        headLeft = loadImage("/Graphics/head_left.png");
        headRight = loadImage("/Graphics/head_right.png");
        apple = loadImage("/Graphics/apple.png");
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

    public void draw(GraphicsContext gc, Snake snake, Food food, boolean gameOver, int width, int height) {
        gc.clearRect(0, 0, width, height);

        // Draw the checkerboard grid
        Color lightGreen = Color.web("#A2D149");
        Color darkGreen = Color.web("#AAD751");
        for (int x = 0; x < width - TILE_SIZE; x += TILE_SIZE) {
            for (int y = 40; y < height - TILE_SIZE; y += TILE_SIZE) {
                if ((x / TILE_SIZE + y / TILE_SIZE) % 2 == 0) {
                    gc.setFill(lightGreen);
                } else {
                    gc.setFill(darkGreen);
                }
                gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw the border
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(0, 0, width, 40);  // Top border
        gc.fillRect(0, 0, TILE_SIZE, height);  // Left border
        gc.fillRect(width - TILE_SIZE, 0, TILE_SIZE, height);  // Right border
        gc.fillRect(0, height - TILE_SIZE, width, TILE_SIZE);  // Bottom border

        // Draw the snake with rounded rectangles and interpolate movement
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point current = snake.getBody().get(i);
            Point previous = snake.getPreviousBody().get(i);

            double startX = previous.getX() * TILE_SIZE;
            double startY = previous.getY() * TILE_SIZE + 40;  // Offset for top border
            double endX = current.getX() * TILE_SIZE;
            double endY = current.getY() * TILE_SIZE + 40;  // Offset for top border

            double x = interpolate(startX, endX);
            double y = interpolate(startY, endY);

            if (i == 0) { // Head
                Image headImage = null;
                switch (snake.getDirection()) {
                    case UP:
                        headImage = headUp;
                        break;
                    case DOWN:
                        headImage = headDown;
                        break;
                    case LEFT:
                        headImage = headLeft;
                        break;
                    case RIGHT:
                        headImage = headRight;
                        break;
                }
                if (headImage != null) {
                    gc.drawImage(headImage, x, y, TILE_SIZE, TILE_SIZE);
                }
            } else { // Body
                gc.setFill(Color.web("#4A73F1"));
                gc.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 10, 10);
            }
        }

        // Draw the food
        food.draw(gc);

        // Draw game over message
        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.fillText("Game Over! Press Enter to Restart", width / 2 - 100, height / 2);
        }
    }

    private double interpolate(double start, double end) {
        double t = ((double) animationStep) / ANIMATION_STEPS;
        t = t * t * (3 - 2 * t); // Smoothstep easing function
        return start + t * (end - start);
    }

    public void setAnimationStep(int step) {
        this.animationStep = step;
    }
}
