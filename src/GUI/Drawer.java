package GUI;

import gameLogic.Game;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;

public class Drawer extends AnimationTimer {
    private Canvas canvas;
    private Game game;
    private Text field1, field2, field3;
    public String string1, string2, string3;
    private boolean stopRequest = false;
    private boolean running = false;

    public Drawer(Canvas canvas, Game game, Text field1, Text field2, Text field3) {
        this.canvas = canvas;
        this.game = game;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    @Override
    public void handle(long now) {
        running = true;
        setFields();

        if (game != null) {
            game.drawGame(canvas);
        } else {
            clear();
            setFields();
            running = false;
            this.stop();
        }
    }

    private void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void setFields() {
        if (field1 != null) {
            field1.setText(string1);
        }
        if (field2 != null) {
            field2.setText(string2);
        }
        if (field3 != null) {
            field3.setText(string3);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setStopRequest(boolean stopRequest) {
        this.stopRequest = stopRequest;
    }
}
