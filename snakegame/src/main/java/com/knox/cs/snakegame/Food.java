package com.knox.cs.snakegame;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Food {
    private Point point;
    private static final Random random = new Random();
    private Timeline bounceTimeline;  //bounce aniamtion
    private int bounceOffset; //offset for bounce animation

    public Food() {
        relocate(null);
        bounceOffset = 0;
        bounceTimeline = new Timeline(new KeyFrame(Duration.millis(350), e -> bounce()));
        bounceTimeline.setCycleCount(Timeline.INDEFINITE);
        bounceTimeline.play();
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
        return new Point(point.getX(), point.getY() + bounceOffset);
    }

    private void bounce() {
        bounceOffset = (bounceOffset == 0) ? 1 : 0;
    }
}
