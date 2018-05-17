package aStar;

import gameLogic.Direction;
import gameLogic.Item;
import gameLogic.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AStarPathFinder {

    private Node[][] nodes;
    private Node head;
    private Node fruit;

    public AStarPathFinder(Point head, Point fruit, Item[][] map) {
        loadNodes(map, head, fruit);
    }

    public Direction evaluateAStar(Direction direction) {
        LinkedList<Node> openList = new LinkedList<>();
        LinkedList<Node> closedList = new LinkedList<>();
        openList.add(head);
        while (true) {
            Node current = getLowestF(openList);
            closedList.add(current);
            openList.remove(current);
            if (current.equals(fruit)) {
                Node next = calculatePath().get(0); //First step of the path
                return head.getDirectionTo(next);
            }

            ArrayList<Node> neighbours = getNeighbours(current);
            for (Node n : neighbours) {
                if(closedList.contains(n))
                    continue;
                if (!openList.contains(n)) {
                    openList.add(n);
                }
            }

            if (openList.isEmpty()) {
                ArrayList<Node> headNeighbours = getNeighbours(head);
                if (headNeighbours.isEmpty())
                    return direction;

                return head.getDirectionTo(headNeighbours.get(0));
            }
        }
    }

    private LinkedList<Node> calculatePath() {
        LinkedList<Node> path = new LinkedList<>();
        for (Node n = fruit; n.getCameFrom() != null; n = n.getCameFrom()) {
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }

    private void loadNodes(Item[][] map, Point head, Point fruit) {
        nodes = new Node[25][25];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                nodes[i][j] = new Node(new Point(j, i), map[i][j]);
            }
        }
        this.fruit = nodes[fruit.y][fruit.x];

        this.head = nodes[head.y][head.x];
        this.head.explore(null, this.fruit);
        this.head.setgCost(0);
        this.head.sethCost(this.head.absDistanceFromOtherPoint(fruit));

    }

    private ArrayList<Node> getNeighbours(Node n) {
        ArrayList<Node> neighbours = new ArrayList<>(4);
        if (n.x != 0) {
            Node current = nodes[n.y][n.x - 1];
            if (!current.isObstacle()) {
                neighbours.add(current);
                current.explore(n, fruit);
            }
        }
        if (n.x != nodes[0].length - 1) {
            Node current = nodes[n.y][n.x + 1];
            if (!current.isObstacle()) {
                neighbours.add(current);
                current.explore(n, fruit);
            }
        }
        if (n.y != 0) {
            Node current = nodes[n.y - 1][n.x];
            if (!current.isObstacle()) {
                neighbours.add(current);
                current.explore(n, fruit);
            }
        }
        if (n.y != nodes.length - 1) {
            Node current = nodes[n.y + 1][n.x];
            if (!current.isObstacle()) {
                neighbours.add(current);
                current.explore(n, fruit);
            }
        }

        return neighbours;
    }

    private Node getLowestF(LinkedList<Node> list) {
        int min = Integer.MAX_VALUE;
        Node minNode = null;
        for (Node n : list) {
            if (n.getfCost() < min) {
                min = n.getfCost();
                minNode = n;
            }
        }

        return minNode;
    }
}
