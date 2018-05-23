package neuralNetworkOpener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import neuralNetwork.Layer;
import neuralNetwork.NeuralNet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Opener {

    @FXML
    private Text topologyText;

    @FXML
    private Text errorText;

    @FXML
    private ComboBox<String> layerBox;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextArea biasText;

    @FXML
    private TextField fileBox;

    @FXML
    private TextArea weightText;

    @FXML
    private AnchorPane resultPane;

    @FXML
    private Button openButton;

    @FXML
    private Text activationText;

    private NeuralNet currentNet;

    @FXML
    void setOnDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.COPY);
    }

    @FXML
    void setOnDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        String fileName = db.getFiles().get(0).getPath();

        fileBox.setText(fileName);
        showNet(fileName);
    }

    @FXML
    void openAction() {
        String fileName = fileBox.getText();
        showNet(fileName);
    }

    @FXML
    void showLayer() {
        for (int i = 0; i < layerBox.getItems().size(); i++) {
            if (layerBox.getItems().get(i).equals(layerBox.getValue())) {
                Layer layer = currentNet.getLayers()[i + 1];
                weightText.setText(layer.getWeights().toString());
                biasText.setText(layer.getBiases().toString());
                activationText.setText(layer.getActivation());
            }
        }
    }

    private void showNet(String fileName) {
        if (openNet(fileName)) {
            errorText.setText("");
            resultPane.setVisible(true);
            StringBuilder topology = new StringBuilder();
            for (int i : currentNet.getTopology()) {
                topology.append(i);
                topology.append(" ");
            }
            topologyText.setText("Topology: " + topology);
            ObservableList<String> connections = FXCollections.observableArrayList();
            for (int i = 1; i < currentNet.getTopology().length; i++) {
                connections.add("Connection " + i);
            }
            layerBox.setItems(connections);
        } else {
            errorText.setText("Can't open file!");
        }
    }

    private boolean openNet(String filename) {
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            currentNet = (NeuralNet) in.readObject();
            in.close();
            file.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
