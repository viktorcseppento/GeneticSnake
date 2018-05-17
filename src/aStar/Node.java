package aStar;

import gameLogic.Item;
import gameLogic.Point;

public class Node extends Point implements Comparable {
    private int gCost; //Kezdőponttól a távolság
    private int hCost; //Végponttól a távolság
    private Node cameFrom; //A csúcsot tartalmazó útvonalon a csúcs előtti csúcs
    private boolean explored;
    private boolean obstacle;


    Node(Point point, Item item) {
        super(point);
        this.gCost = Integer.MAX_VALUE / 2;
        this.hCost = Integer.MAX_VALUE / 2;
        this.cameFrom = null;
        obstacle = (item.equals(Item.WALL) || item.equals(Item.SNAKE));
    }

    void explore(Node cameFrom, Node fruit) {
        explored = true;

        if(cameFrom == null)
            this.gCost = 0;

        else if(cameFrom.absDistanceFromOtherPoint(this) < this.gCost) {
            this.gCost = cameFrom.absDistanceFromOtherPoint(this);
            this.cameFrom = cameFrom;
        }

        this.hCost = fruit.absDistanceFromOtherPoint(this);
    }

    @Override
    public int compareTo(Object o) {
        Node other = (Node) o;
        if (this.getfCost() == other.getfCost())
            return this.gethCost() - other.gethCost();
        else
            return this.getfCost() - other.getfCost();
    }

    int getfCost() {
        return gCost + hCost;
    }

    private int gethCost() {
        return hCost;
    }

    Node getCameFrom() {
        return cameFrom;
    }

    void setgCost(int gCost) {
        this.gCost = gCost;
    }

    void sethCost(int hCost) {
        this.hCost = hCost;
    }

    boolean isObstacle() {
        return obstacle;
    }
}
