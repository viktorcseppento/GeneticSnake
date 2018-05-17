package algorithms;

import GUI.Drawer;
import evolution.Agent;
import gameLogic.AIMethod;
import gameLogic.Game;
import gameLogic.GameConfig;
import gameLogic.Snake;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;
import neuralNetwork.NeuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeedAlgorithm extends Thread {
    private Canvas canvas;
    private Text scoreText;
    private int FPS = 60;
    private GameConfig gameConfig = new GameConfig();
    private Agent agent;

    public SeedAlgorithm(Canvas canvas, Text scoreText, String seed) {
        this.canvas = canvas;
        this.scoreText = scoreText;
        agent = loadAgent(seed);
    }

    private Agent loadAgent(String seed) {
        NeuralNet neuralNet = null;
        try {
            File directory = new File("neuralNetworks");
            String[] fileNames = directory.list();
            String fileName = "";
            Pattern pattern = Pattern.compile(seed);
            if (fileNames != null) {
                for (String name : fileNames) {
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.find()) {
                        fileName = name;
                        break;
                    }
                }
                if (!fileName.equals("")) {
                    FileInputStream file = new FileInputStream("neuralNetworks/" + fileName);
                    ObjectInputStream in = new ObjectInputStream(file);

                    neuralNet = (NeuralNet) in.readObject();
                    in.close();
                    file.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(neuralNet != null){
            return new Agent(neuralNet, Long.valueOf(seed));
        }
        return null;
    }

    @Override
    public void run() {
        Snake snake = new Snake(AIMethod.GENETIC, agent);
        Game game = new Game(snake, gameConfig.getMap());
        Drawer drawer = new Drawer(canvas, game, scoreText, null, null);
        drawer.start();
        long lastUpdated = 0;

        while (game.isRunning()) {
            if (isInterrupted()) {
                game.stop();
                drawer.stop();
                break;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdated > 1000 / FPS) {
                lastUpdated = currentTime;
                game.step();
                drawer.string1 = "Score: " + String.valueOf(snake.getLen() - 3);
            }
        }
        drawer.stop();
    }

    public Agent getAgent() {
        return agent;
    }
}
