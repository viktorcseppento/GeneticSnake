package gameLogic;

import aStar.Node;

import static java.lang.Math.abs;

public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;

    }

    static Point add(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    private void moveUp() {
        y--;
    }

    private void moveRight() {
        x++;
    }

    private void moveDown() {
        y++;
    }

    private void moveLeft() {
        x--;
    }

    void translate(Direction direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case RIGHT:
                moveRight();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
        }
    }

    void translate(Point vector) {
        x += vector.x;
        y += vector.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int absDistanceFromOtherPoint(Point other) {
        int xDifference = abs(x - other.x);
        int yDifference = abs(y - other.y);
        return xDifference + yDifference;
    }

    public Direction getDirectionTo(Node to) {
        if (to.x > this.x)
            return Direction.RIGHT;
        else if (to.x < this.x)
            return Direction.LEFT;
        else if (to.y > this.y)
            return Direction.DOWN;
        else
            return Direction.UP;
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
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
