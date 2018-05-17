package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

    public static void main(String[] args) {

        try {
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        window.setResizable(false);
        window.setTitle("Genetic Snake");
        window.setScene(new Scene(root, 600, 400));
        window.show();
    }
}
