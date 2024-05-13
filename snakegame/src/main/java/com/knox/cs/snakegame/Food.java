package com.knox.cs.snakegame;

import java.util.Random;

public class Food {
    private Point point;
    private static final Random random = new Random();
    
    public Food() {
        relocate(null);
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
}