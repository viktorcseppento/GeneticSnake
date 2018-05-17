package algorithms;

import GUI.Drawer;
import gameLogic.AIMethod;
import gameLogic.Game;
import gameLogic.GameConfig;
import gameLogic.Snake;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;

public class AStarAlgorithm extends Thread {

    private Canvas canvas;
    private Text scoreText;
    private int FPS = 60;
    private GameConfig gameConfig = new GameConfig();

    public AStarAlgorithm(Canvas canvas, Text scoreText) {
        this.canvas = canvas;
        this.scoreText = scoreText;
    }

    @Override
    public void run() {
        Snake snake = new Snake(AIMethod.ASTAR, null);
        Game game = new Game(snake, gameConfig.getMap());
        Drawer drawer = new Drawer(canvas, game, scoreText, null, null);
        drawer.start();
        long lastUpdated = 0;

        while (game.isRunning()) {
            if(isInterrupted()) {
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

}
