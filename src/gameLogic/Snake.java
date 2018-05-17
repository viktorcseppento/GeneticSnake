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
    private int lifeTime = 0;
    private Set<Point> explored = new HashSet<>(); //Explored points
    private int leftTurns = 0;
    private int rightTurns = 0;

    public Snake(AIMethod method, Agent brain) {
        this.brain = brain;
        this.method = method;
    }


    void move() {
        health--;
        lifeTime++;

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
        //  fitness *= min(leftTurns, rightTurns) / (max(leftTurns, rightTurns));
          /*  if (getLen() > 10)
                if (max(leftTurns, rightTurns) / (1 + min(leftTurns, rightTurns)) > 2.2)
                    fitness = 0;
        */

        return fitness;
    }

    public Agent getBrain() {
        return brain;
    }

    public NeuralNet getNet() {
        return brain.getNet();
    }

    public Point getHead() {
        return points.get(0);
    }

    public Point getLast() {
        return points.get(points.size() - 1);
    }

    public Direction getDirection() {
        return direction;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void requestNewPoint() {
        requestedNewPoint = true;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public AIMethod getMethod() {
        return method;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLen() {
        return points.size();
    }

    public Set<Point> getExplored() {
        return explored;
    }

    public void explore(Point point) {
        explored.add(point);
    }

    public void addLeftTurn() {
        leftTurns++;
    }

    public void addRightTurn() {
        rightTurns++;
    }

    public int getLeftTurns() {
        return leftTurns;
    }

    public int getRightTurns() {
        return rightTurns;
    }
}
