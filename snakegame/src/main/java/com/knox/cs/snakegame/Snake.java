package com.knox.cs.snakegame;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private List<Point> previousBody;
    private Direction direction;
    private boolean alive;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(10, 10));
        previousBody = new LinkedList<>(body);
        direction = Direction.RIGHT;
        alive = true;
    }

    public void move() {
        previousBody = new LinkedList<>(body); // Store current body as previous body
        Point head = body.get(0);
        Point newHead = head.move(direction);

        int boardWidth = GameBoard.WIDTH / GameBoard.TILE_SIZE;
        int boardHeight = (GameBoard.HEIGHT - 40) / GameBoard.TILE_SIZE; // Adjust for the border

        if (newHead.isOutOfBounds(boardWidth, boardHeight) || body.contains(newHead)) {
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

    public List<Point> getPreviousBody() {
        return previousBody;
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
