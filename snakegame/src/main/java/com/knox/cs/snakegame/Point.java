package com.knox.cs.snakegame;

import java.util.Objects;

public class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Point move(Direction direction) {
        switch (direction) {
            case UP:
                return new Point(x, y - 1);
            case DOWN:
                return new Point(x, y + 1);
            case LEFT:
                return new Point(x - 1, y);
            case RIGHT:
                return new Point(x + 1, y);
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }
    
    public boolean isOutOfBounds(int width, int height) {
        // Exclude the border tiles (1 tile thick border on sides, 2 tiles thick border on top)
        return x < 1 || x >= width - 1 || y < 0 || y >= height - 1;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}