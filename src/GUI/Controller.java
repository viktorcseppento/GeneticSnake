package GUI;

import algorithms.AStarAlgorithm;
import algorithms.SeedAlgorithm;
import evolution.EvolutionConfig;
import algorithms.GeneticAlgorithm;
import algorithms.ResultAlgorithm;
import gameLogic.GameConfig;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import neuralNetwork.NeuralConfig;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller implements Initializable {

    private AStarAlgorithm aStarAlgorithm;

    private GeneticAlgorithm geneticAlgorithm;

    private ResultAlgorithm resultAlgorithm;

    private SeedAlgorithm seedAlgorithm;

    @FXML
    private Canvas geneticCanvas;

    @FXML
    private Text resultPopSize;

    @FXML
    private CheckBox geneticSpeedBox;

    @FXML
    private Button seedButton;

    @FXML
    private Canvas seedCanvas;

    @FXML
    private Text geneticIndividualField;

    @FXML
    private TextField replayField;

    @FXML
    private TextField elitesText;

    @FXML
    private ChoiceBox<String> activationBox;

    @FXML
    private Button geneticButton;

    @FXML
    private TextField seedField;

    @FXML
    private TextField popSizeText;

    @FXML
    private CheckBox geneticSerializeBox;

    @FXML
    private Text seedErrorText;

    @FXML
    private Text seedScoreText;

    @FXML
    private Text aStarScoreField;

    @FXML
    private Text geneticGenerationField;

    @FXML
    private Canvas resultCanvas;

    @FXML
    private Text resultScoreText;

    @FXML
    private TextField topologyText;

    @FXML
    private TextField mutRateText;

    @FXML
    private Text resultErrorText;

    @FXML
    private TextField mapNameText;

    @FXML
    private Text geneticScoreField;

    @FXML
    private Tab resultTab;

    @FXML
    private Button replayButton;

    @FXML
    private Text resultNoGen;

    @FXML
    private Canvas aStarCanvas;

    @FXML
    private Button aStarButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activationBox.setItems(FXCollections.observableArrayList(
                "SIGMOID", "TANH", "RELU"));
        activationBox.setValue("SIGMOID");
    }

    @FXML
    void aStarStartAction() {
        aStarScoreField.setText("Score: ");
        if (aStarAlgorithm != null) {
            aStarButton.setText("Start");
            aStarAlgorithm.interrupt();
            aStarAlgorithm = null;
            aStarCanvas.getGraphicsContext2D().clearRect(0, 0, aStarCanvas.getWidth(), aStarCanvas.getHeight());
        } else {
            aStarButton.setText("Stop");
            setPreferences();

            aStarAlgorithm = new AStarAlgorithm(aStarCanvas, aStarScoreField);
            aStarAlgorithm.start();
        }
    }

    @FXML
    void seedAction() {
        if (seedAlgorithm != null) {
            seedScoreText.setText("");
            seedButton.setText("Start");
            seedAlgorithm.interrupt();
            seedAlgorithm = null;
            seedCanvas.getGraphicsContext2D().clearRect(0, 0, seedCanvas.getWidth(), seedCanvas.getHeight());
        } else {
            setPreferences();
            seedAlgorithm = new SeedAlgorithm(seedCanvas, seedScoreText, seedField.getText());
            if (seedAlgorithm.getAgent() == null) {
                seedErrorText.setText("Invalid seed");
                seedAlgorithm = null;
                return;
            }
            seedButton.setText("Stop");
            seedAlgorithm.start();
            seedErrorText.setText("");
        }
    }


    @FXML
    void geneticStartAction() {
        if (geneticAlgorithm != null && geneticAlgorithm.isRunning()) {
            geneticButton.setText("Start");
            resultNoGen.setText("Number of generations: " + (geneticAlgorithm.getPopulations().size() - 1));
            resultPopSize.setText("Generation size: " + geneticAlgorithm.getEvolutionConfig().getPopulationSize());
            resultAlgorithm = new ResultAlgorithm(geneticAlgorithm, resultCanvas, replayField, resultScoreText, resultErrorText);
            geneticAlgorithm.requestToStop();
            resultTab.setDisable(false);
        } else {
            geneticButton.setText("Stop");
            setPreferences();

            geneticAlgorithm = new GeneticAlgorithm(geneticCanvas, geneticGenerationField, geneticIndividualField, geneticScoreField);
            geneticSpeedBox.setSelected(false);
            geneticSerializeBox.setSelected(false);
            geneticAlgorithm.start();
        }
    }

    @FXML
    void geneticSpeedAction() {
        if (geneticAlgorithm != null) {
            if (geneticSpeedBox.isSelected()) {
                geneticAlgorithm.setFPS(Integer.MAX_VALUE);
            } else {
                geneticAlgorithm.setFPS(60);
            }
        }
    }

    @FXML
    void geneticSerializeAction() {
        if (geneticAlgorithm != null) {
            if (geneticSerializeBox.isSelected()) {
                geneticAlgorithm.setSerialize(true);
            } else {
                geneticAlgorithm.setSerialize(false);
            }
        }
    }

    @FXML
    void replayAction() {
        if (!resultAlgorithm.isRunning()) {
            replayButton.setText("Stop");
            resultAlgorithm.start();
        } else {
            resultAlgorithm.interrupt();
            resultScoreText.setText("");
            resultAlgorithm = new ResultAlgorithm(resultAlgorithm);
            replayButton.setText("Replay");
            resultCanvas.getGraphicsContext2D().clearRect(0, 0, resultCanvas.getWidth(), resultCanvas.getHeight());
        }
    }


    private void setPreferences() {
        Preferences gamePref = Preferences.userNodeForPackage(GameConfig.class);
        gamePref.put("mapName", mapNameText.getText() + ".txt");

        Preferences evolutionPref = Preferences.userNodeForPackage(EvolutionConfig.class);
        evolutionPref.put("populationSize", popSizeText.getText());
        evolutionPref.put("mutationRate", mutRateText.getText());
        evolutionPref.put("numberOfElites", elitesText.getText());

        Preferences neuralPref = Preferences.userNodeForPackage(NeuralConfig.class);
        neuralPref.put("activation", activationBox.getValue());
        neuralPref.put("topology", topologyText.getText());
    }

}
