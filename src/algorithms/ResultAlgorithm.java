package algorithms;

import GUI.Drawer;
import evolution.Population;
import gameLogic.AIMethod;
import gameLogic.Game;
import gameLogic.GameConfig;
import gameLogic.Snake;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultAlgorithm extends Thread {
    private ArrayList<Population> populations; //Every agent throughout the algorithm
    private GameConfig gameConfig;
    private Canvas canvas;
    private TextField replayField;
    private int FPS = 60;
    private boolean isRunning = false;
    private Text scoreText, errorText;


    public ResultAlgorithm(GeneticAlgorithm geneticAlgorithm, Canvas canvas, TextField replayField, Text scoreText, Text errorText) {
        this.populations = geneticAlgorithm.getPopulations();
        this.gameConfig = geneticAlgorithm.getGameConfig();
        this.canvas = canvas;
        this.replayField = replayField;
        this.scoreText = scoreText;
        this.errorText = errorText;
    }

    public ResultAlgorithm(ResultAlgorithm resultAlgorithm) {
        this.populations = resultAlgorithm.populations;
        this.gameConfig = resultAlgorithm.gameConfig;
        this.canvas = resultAlgorithm.canvas;
        this.replayField = resultAlgorithm.replayField;
        this.scoreText = resultAlgorithm.scoreText;
        this.errorText = resultAlgorithm.errorText;
    }

    @Override
    public void run() {
        isRunning = true;
        String pattern = "\\d+-\\d+";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(replayField.getText());
        String valid;
        if (matcher.find()) {
            valid = matcher.group(0);
        } else {
            errorText.setText("Invalid format");
            return;
        }
        StringTokenizer st = new StringTokenizer(valid, "-");
        int generation = Integer.parseInt(st.nextToken());
        int individual = Integer.parseInt(st.nextToken());
        if (generation < 1 || generation > populations.size() || individual < 1 || individual > populations.get(0).getAgents().length) {
            errorText.setText("Invalid input");
            return;
        }
        errorText.setText("");
        Snake snake = new Snake(AIMethod.GENETIC, populations.get(generation - 1).getAgents()[individual - 1]);
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

    public boolean isRunning() {
        return isRunning;
    }
}
