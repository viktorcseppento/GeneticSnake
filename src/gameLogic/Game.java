package gameLogic;

import aStar.AStarPathFinder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import neuralNetwork.Matrix;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Snake snake;
    private Point fruit;
    private Item[][] map;
    private boolean running = true;

    public Game(Snake snake, Item[][] map) {
        this.snake = snake;
        this.map = new Item[25][25];
        for (int i = 0; i < 25; i++) {
            System.arraycopy(map[i], 0, this.map[i], 0, map[0].length);
        }

        int middleCol = this.map.length / 2;
        int middleRow = this.map[0].length / 2;
        //Place down snake
        snake.getPoints().add(new Point(middleCol, middleRow)); //Head
        this.map[middleRow][middleCol] = Item.SNAKE;
        snake.getPoints().add(new Point(middleCol - 1, middleRow));
        this.map[middleRow][middleCol - 1] = Item.SNAKE;
        snake.getPoints().add(new Point(middleCol - 2, middleRow));
        this.map[middleRow][middleCol - 2] = Item.SNAKE;

        snake.explore(snake.getHead());
        generateFruit();
    }

    /**
     * Steps the game one frame ahead.
     */
    public void step() {
        if (!running)
            return;

        Direction oldDirection = snake.getDirection();

        if (snake.getMethod().equals(AIMethod.GENETIC))
            snake.setDirection(evaluateNet());
        else if (snake.getMethod().equals(AIMethod.ASTAR)) {
            AStarPathFinder pathFinder = new AStarPathFinder(snake.getHead(), fruit, map);
            snake.setDirection(pathFinder.evaluateAStar(snake.getDirection()));
        }else if (snake.getMethod().equals(AIMethod.DEEPQ)){
            snake.setDirection(evaluateNet());
        }

        if (oldDirection.toTheLeft().equals(snake.getDirection()))
            snake.addLeftTurn();
        if (oldDirection.toTheRight().equals(snake.getDirection()))
            snake.addRightTurn();

        Point oldLast = snake.getLast();

        snake.move();
        snake.explore(snake.getHead());
        Point nowLast = snake.getLast();
        Point head = snake.getHead();

        //If hits the wall or itself, die
        if (map[head.y][head.x].equals(Item.WALL) || map[head.y][head.x].equals(Item.SNAKE)) {
            //If hits obstacle, die.
            snake.die();
            running = false;
            return;
        }

        //Regenerate the map
        map[head.y][head.x] = Item.SNAKE;
        if (!oldLast.equals(nowLast))
            map[oldLast.y][oldLast.x] = Item.NOTHING;


        //If hits fruit, get longer and generate a new one
        if (snake.getPoints().get(0).equals(fruit)) {
            snake.setHealth(200);
            snake.requestNewPoint();
            generateFruit();
        }

        if (snake.getHealth() == 0) {
            snake.die();
            running = false;
        }
    }

    public void drawGame(Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double cellSize = canvas.getWidth() / 25;
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                switch (map[i][j]) {
                    /*case SNAKE:
                        if(snake.getHead().x == j && snake.getHead().y == i)
                            gc.setFill(Color.BLUE);
                        else
                            gc.setFill(Color.GREEN);
                        gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                        break;*/
                    case WALL:
                        gc.setFill(Color.BLACK);
                        gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                        break;
                    case FRUIT:
                        gc.setFill(Color.RED);
                        gc.fillOval(j * cellSize, i * cellSize, cellSize, cellSize);
                        break;
                }
            }
        }
        for (int i = 0; i < snake.getPoints().size(); i++) {
            double greenValue = 0.5 + 0.5 * i / snake.getPoints().size();
            gc.setFill(Color.color(0, greenValue, 0));
            gc.fillOval(snake.getPoints().get(i).x * cellSize, snake.getPoints().get(i).y * cellSize, cellSize, cellSize);
        }
    }

    private Direction evaluateNet() {

        Matrix input = Matrix.oneDimensionalArrayToVector(generateInputs(), true);
        double[] output = snake.getNet().feedForward(input).toOneDimensionalArray();

        if (output[0] > output[1] && output[0] > output[2]) {
            return snake.getDirection();
        } else if (output[1] > output[2]) {
            return snake.getDirection().toTheRight();
        } else {
            return snake.getDirection().toTheLeft();
        }
    }


    private double[] generateInputs() {
        /*      left wall/snake, left fruit,
                left diagonal wall/snake, left diagonal fruit,
                forward wall/snake, forward fruit,
                right diagonal wall/snake, right diagonal fruit,
                right wall/snake right fruit;
        */
        double[] neuralNetInputs = new double[13];

        for (int i = 0; i < neuralNetInputs.length; i++) {
            Point discoverer = new Point(snake.getHead());
            Item what = Item.values()[i % 2];
            Point directionVector;
            if (i < 2) {
                directionVector = snake.getDirection().toTheLeft().getVector();
                neuralNetInputs[i] = reciprocalDistanceToItem(discoverer, directionVector, what);
            } else if (i < 4) {
                directionVector = Point.add(snake.getDirection().toTheLeft().getVector(), snake.getDirection().getVector());
                neuralNetInputs[i] = reciprocalDistanceToItem(discoverer, directionVector, what);
            } else if (i < 6) {
                directionVector = snake.getDirection().getVector();
                neuralNetInputs[i] = reciprocalDistanceToItem(discoverer, directionVector, what);
            } else if (i < 8) {
                directionVector = Point.add(snake.getDirection().getVector(), snake.getDirection().toTheRight().getVector());
                neuralNetInputs[i] = reciprocalDistanceToItem(discoverer, directionVector, what);
            } else if (i < 10) {
                directionVector = snake.getDirection().toTheRight().getVector();
                neuralNetInputs[i] = reciprocalDistanceToItem(discoverer, directionVector, what);
            } else {
                switch (i) {
                    case 10:
                        discoverer.translate(snake.getDirection().toTheLeft());
                        break;
                    case 11:
                        discoverer.translate(snake.getDirection());
                        break;
                    case 12:
                        discoverer.translate(snake.getDirection().toTheRight());
                        break;
                }
                if (map[discoverer.y][discoverer.x].equals(Item.WALL) || map[discoverer.y][discoverer.x].equals(Item.SNAKE)) {
                    neuralNetInputs[i] = 0;
                } else if (snake.getExplored().contains(discoverer))
                    neuralNetInputs[i] = -1;
                else
                    neuralNetInputs[i] = 1;
            }
        }

        return neuralNetInputs;
    }

    /**
     * Reciprocal of how far is the closest specified item to a specific point in the specified direction.
     * Returns 0, if there is no such item.
     *
     * @param discoverer      specified point.
     * @param directionVector vector of the specified direction.
     * @param what            specified item.
     * @return the reciprocal of the distance from the point to the item in the direction, 0 if couldn't find the item.
     */
    private double reciprocalDistanceToItem(Point discoverer, Point directionVector, Item what) {
        int distance = 0;
        discoverer.translate(directionVector);
        while (inBounds(discoverer)) {
            distance++;
            if (what.equals(Item.FRUIT)) {
                if (map[discoverer.y][discoverer.x].equals(Item.WALL) || map[discoverer.y][discoverer.x].equals(Item.SNAKE))
                    return -1;
                else if (map[discoverer.y][discoverer.x].equals(Item.FRUIT))
                    return 1 / distance;
            } else if (what.equals(Item.WALL)) {
                if (map[discoverer.y][discoverer.x].equals(Item.SNAKE) || map[discoverer.y][discoverer.x].equals(Item.WALL))
                    return 1 / distance;
                else if (map[discoverer.y][discoverer.x].equals(Item.FRUIT))
                    return -1;
            }
            discoverer.translate(directionVector);
        }

        return 0;
    }

    private boolean inBounds(Point point) {
        return point.x >= 0 && point.x < map[0].length && point.y >= 0 && point.y < map.length;
    }

    private void generateFruit() {
        ArrayList<Point> freePlaces = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].equals(Item.NOTHING))
                    freePlaces.add(new Point(j, i)); //Put the free places to an array list
            }
        }
        Random r;
        if (!snake.getMethod().equals(AIMethod.ASTAR))
            r = new Random(snake.getBrain().getSeed());
        else
            r = new Random();
        //Set the fruit to a random free place
        fruit = new Point(freePlaces.get(r.nextInt(freePlaces.size())));
        map[fruit.y][fruit.x] = Item.FRUIT;
    }

    public Snake getSnake() {
        return snake;
    }

    public Point getFruit() {
        return fruit;
    }

    public Item[][] getMap() {
        return map;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }
}
