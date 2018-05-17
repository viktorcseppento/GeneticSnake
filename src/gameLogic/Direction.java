package gameLogic;

public enum Direction {
    UP(0), RIGHT(1), DOWN(2), LEFT(3);

    private int ID;

    Direction(int ID) {
        this.ID = ID;
    }

    public Direction toTheRight() {
        return Direction.values()[(ID + 1) % 4];
    }

    public Direction toTheLeft() {
        return Direction.values()[(ID + 3) % 4];
    }

    public Point getVector(){
        switch (this){
            case UP:
                return new Point(0, -1);
            case RIGHT:
                return new Point(1, 0);
            case DOWN:
                return new Point(0, 1);
            case LEFT:
                return new Point(-1, 0);
            default:
                //Default: UP
                return new Point(0, -1);
        }
    }
}
