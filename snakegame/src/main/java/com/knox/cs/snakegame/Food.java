package com.knox.cs.snakegame;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Food {
    private Point point;
    private static final Random random = new Random();
    private Timeline palpitateTimeline;
    private double scale;

    public Food() {
        relocate(null);
        scale = 1.0;

        // Create a timeline that adjusts the scale for the palpitation effect
        palpitateTimeline = new Timeline(
            new KeyFrame(Duration.millis(0), e -> setScale(1.0)),
            new KeyFrame(Duration.millis(500), e -> setScale(1.2)),
            new KeyFrame(Duration.millis(1000), e -> setScale(1.0))
        );
        palpitateTimeline.setCycleCount(Timeline.INDEFINITE);
        palpitateTimeline.play();
    }

    public void relocate(Snake snake) {
        int x = random.nextInt(30);
        int y = random.nextInt(20);
        point = new Point(x, y);

        if (snake != null) {
            while (snake.getBody().contains(point)) {
                x = random.nextInt(30);
                y = random.nextInt(20);
                point = new Point(x, y);
            }
        }
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
        double y = point.getY() * GameBoard.TILE_SIZE;
        double size = GameBoard.TILE_SIZE * scale;

        gc.setFill(Color.RED);
        gc.fillOval(x + (GameBoard.TILE_SIZE - size) / 2, y + (GameBoard.TILE_SIZE - size) / 2, size, size);
    }
}
