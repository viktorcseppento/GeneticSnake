package neuralNetworkOpener;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import neuralNetwork.Matrix;

public class OpenerApp extends Application {

    public static void main(String[] args) {

        try {
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Opener.fxml"));
        window.setResizable(false);
        window.setTitle("Neural Network Opener");
        window.setScene(new Scene(root, 600, 400));
        window.show();
    }
}
