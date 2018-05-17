package algorithms;

import GUI.Drawer;
import evolution.Agent;
import evolution.EvolutionConfig;
import evolution.Population;
import gameLogic.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;
import neuralNetwork.NeuralConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GeneticAlgorithm extends Thread {
    private ArrayList<Population> populations; //Every agent throughout the algorithm
    private Population currentGeneration; //Current population
    private EvolutionConfig evolutionConfig;
    private NeuralConfig neuralConfig;
    private GameConfig gameConfig;
    private boolean stopRequest;
    private boolean running = true;
    private Canvas canvas;
    private int FPS = 60;
    private int noIndividual = 0;
    private int noGeneration = 1;
    private Drawer drawer;
    private boolean serialize = false;

    public GeneticAlgorithm(Canvas canvas, Text generationField, Text individualField, Text scoreField) {
        evolutionConfig = new EvolutionConfig();
        neuralConfig = new NeuralConfig();
        gameConfig = new GameConfig();
        currentGeneration = Population.createRandomGeneration(evolutionConfig, neuralConfig);
        populations = new ArrayList<>(1);
        populations.add(currentGeneration);
        stopRequest = false;
        this.canvas = canvas;
        drawer = new Drawer(canvas, null, generationField, individualField, scoreField);

    }

    public void run() {
        drawer.start();
        while (true) {
            if (stopRequest) {
                populations.remove(currentGeneration);
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                running = false;
                if (serialize) {
                    Serializer serializer = new Serializer(populations, neuralConfig, gameConfig, evolutionConfig);
                    serializer.serialize();
                }
                break;
            }

            if (currentGeneration.evaluated()) {
                Arrays.sort(currentGeneration.getAgents(), Collections.reverseOrder());
                Population newPop = currentGeneration.createNewGeneration(evolutionConfig, neuralConfig);
                populations.add(newPop);
                currentGeneration = newPop;
                noGeneration++;
            }

            for (int i = 0; i < currentGeneration.getAgents().length; i++) {
                noIndividual = i + 1;
                Agent a = currentGeneration.getAgents()[i];
                evaluate(a);
                if (stopRequest) {
                    break;
                }
            }
        }
    }

    private void evaluate(Agent a) {
        Snake snake = new Snake(AIMethod.GENETIC, a);
        Game game = new Game(snake, gameConfig.getMap());
        drawer.setGame(game);
        if (!stopRequest) {
            drawer.string1 = "Generation: " + String.valueOf(noGeneration);
            drawer.string2 = "Individual: " + String.valueOf(noIndividual);
        }

        drawer.start();

        long lastUpdated = 0;
        while (game.isRunning()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdated > 1000 / FPS) {
                if (stopRequest) {
                    drawer.string1 = "Generation: ";
                    drawer.string2 = "Individual: ";
                    drawer.string3 = "Score: ";
                    game.stop();
                    break;
                } else
                    drawer.string3 = "Score: " + String.valueOf(snake.getLen() - 3);
                lastUpdated = currentTime;
                game.step();
            }
        }
        drawer.setGame(null);
        drawer.setStopRequest(true);
        a.setEvaluated(true);

    }

    public void requestToStop() {
        running = false;
        stopRequest = true;
    }

    public EvolutionConfig getEvolutionConfig() {
        return evolutionConfig;
    }

    public NeuralConfig getNeuralConfig() {
        return neuralConfig;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public ArrayList<Population> getPopulations() {
        return populations;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }
}

