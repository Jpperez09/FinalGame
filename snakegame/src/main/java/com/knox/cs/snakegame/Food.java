package com.knox.cs.snakegame;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Food {
    private Point point;
    private static final Random random = new Random();
    private Timeline palpitateTimeline;
    private double scale;
    private Image apple;

    public Food(Snake snake) {
        relocate(snake);
        scale = 1.0;

        // Load apple image
        apple = loadImage("/Graphics/apple.png");

        // Create a timeline that adjusts the scale for the palpitation effect
        palpitateTimeline = new Timeline(
            new KeyFrame(Duration.millis(0), e -> setScale(1.0)),
            new KeyFrame(Duration.millis(500), e -> setScale(1.2)),
            new KeyFrame(Duration.millis(1000), e -> setScale(1.0))
        );
        palpitateTimeline.setCycleCount(Timeline.INDEFINITE);
        palpitateTimeline.play();
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

    public void relocate(Snake snake) {
        int x, y;
        do {
            x = random.nextInt(15) + 1; // Ensure it's within the grid excluding borders
            y = random.nextInt(12) + 2; // Ensure it's within the grid excluding borders
            point = new Point(x, y);
        } while (snake.getBody().contains(point));
    }

    public Point getPoint() {
        return point;
    }

    public double getScale() {
        return scale;
    }

    private void setScale(double scale) {
        this.scale = scale;
    }

    public void draw(GraphicsContext gc) {
        double x = point.getX() * GameBoard.TILE_SIZE;
        double y = point.getY() * GameBoard.TILE_SIZE + 40;  // Offset for top border

        double size = GameBoard.TILE_SIZE * scale;

        gc.drawImage(apple, x + (GameBoard.TILE_SIZE - size) / 2, y + (GameBoard.TILE_SIZE - size) / 2, size, size);
    }
}
