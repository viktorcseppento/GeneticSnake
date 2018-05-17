package gameLogic;

import evolution.Agent;
import neuralNetwork.NeuralNet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.*;

public class Snake {
    private Agent brain;
    private Direction direction = Direction.RIGHT;
    private ArrayList<Point> points = new ArrayList<>();
    private boolean requestedNewPoint = false;
    private AIMethod method;
    private int health = 200;
    private Set<Point> explored = new HashSet<>(); //Explored points
    public Snake(AIMethod method, Agent brain) {
        this.brain = brain;
        this.method = method;
    }


    void move() {
        health--;

        Point tail = new Point(getLast());

        //Move the tail
        for (int i = points.size() - 1; i > 0; i--) {
            points.set(i, new Point(points.get(i - 1)));
        }

        //Set the new point (if exists) to the previous position of last one
        if (requestedNewPoint) {
            points.add(tail);
            requestedNewPoint = false;
        }

        //Then move the head
        getHead().translate(direction);
    }

    void die() {
        if (method.equals(AIMethod.GENETIC))
            brain.setFitness(calcFitness());
    }

    private double calcFitness() {
        double fitness = 10*log(10*explored.size());

        if (getLen() < 8){
            fitness*=pow(2, getLen()-3);
        }else
            fitness*=256*(getLen()-7);
        return fitness;
    }

    Agent getBrain() {
        return brain;
    }

    NeuralNet getNet() {
        return brain.getNet();
    }

    Point getHead() {
        return points.get(0);
    }

    Point getLast() {
        return points.get(points.size() - 1);
    }

    Direction getDirection() {
        return direction;
    }

    ArrayList<Point> getPoints() {
        return points;
    }

    void requestNewPoint() {
        requestedNewPoint = true;
    }

    void setDirection(Direction direction) {
        this.direction = direction;
    }

    AIMethod getMethod() {
        return method;
    }

    int getHealth() {
        return health;
    }

    void heal() {
        this.health = 200;
    }

    public int getLen() {
        return points.size();
    }

    Set<Point> getExplored() {
        return explored;
    }

    void explore(Point point) {
        explored.add(point);
    }

}
