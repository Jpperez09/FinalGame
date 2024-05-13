package com.knox.cs.snakegame;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Direction direction;
    private boolean alive;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(10, 10));
        direction = Direction.RIGHT;
        alive = true;
    }

    public void move() {
        Point head = body.get(0);
        Point newHead = head.move(direction);

        if (newHead.isOutOfBounds(30, 20) || body.contains(newHead)) {
            alive = false;
            return;
        }

        body.add(0, newHead);
        body.remove(body.size() - 1);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isAlive() {
        return alive;
    }

    public List<Point> getBody() {
        return body;
    }

    public boolean isFoodEaten(Food food) {
        Point head = body.get(0);
        if (head.equals(food.getPoint())) {
            body.add(body.get(body.size() - 1)); // grow the snake
            return true;
        }
        return false;
    }
}
