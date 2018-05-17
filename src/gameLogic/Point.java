package gameLogic;

import aStar.Node;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Point {

    public int x;
    public int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;

    }

    public static Point add(Point p1, Point p2) {
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

    void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    void translate(Point vector) {
        x += vector.x;
        y += vector.y;
    }

    public void setPoint(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double diagonalDistanceFromOtherPoint(Point other) {
        int xDifference = abs(x - other.x);
        int yDifference = abs(y - other.y);
        return sqrt(xDifference * xDifference + yDifference * yDifference);
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

    boolean intersect(Point other, int diameter) {
        return diagonalDistanceFromOtherPoint(other) < diameter;
    }

    void translateOnePixelToTargetPos(Point other) {
        if (other.x > x)
            translate(1, 0);
        else if (other.x < x)
            translate(-1, 0);

        if (other.y > y)
            translate(0, 1);
        else if (other.y < y)
            translate(0, -1);
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
