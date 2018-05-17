package algorithms;

import evolution.EvolutionConfig;
import evolution.Population;
import gameLogic.GameConfig;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import neuralNetwork.NeuralConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class Serializer {
    private ArrayList<Population> populations;
    private NeuralConfig neuralConfig;
    private GameConfig gameConfig;
    private EvolutionConfig evolutionConfig;

    Serializer(ArrayList<Population> populations, NeuralConfig neuralConfig, GameConfig gameConfig, EvolutionConfig evolutionConfig) {
        this.populations = populations;
        this.neuralConfig = neuralConfig;
        this.gameConfig = gameConfig;
        this.evolutionConfig = evolutionConfig;
    }

    void serialize() {
        try {
            String fileName = uniqueFileName();
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet resultSheet = workbook.createSheet("Results", 0);

            ArrayList<WritableCell> resultCells = new ArrayList<>();
            int index = 1;
            resultCells.add(new Label(0, 0, "Generation"));
            resultCells.add(new Label(1, 0, "Individual"));
            resultCells.add(new Label(2, 0, "Fitness"));
            resultCells.add(new Label(3, 0, "Seed"));
            for (int i = 0; i < populations.size(); i++) {
                for (int j = 0; j < populations.get(i).getAgents().length; j++) {
                    int fitness = (int) populations.get(i).getAgents()[j].getFitness();
                    if (fitness < 200)
                        continue;
                    try {
                        FileOutputStream file = new FileOutputStream("neuralNetworks/" + fitness + "_" + populations.get(i).getAgents()[j].getSeed() + ".nn");
                        ObjectOutputStream out = new ObjectOutputStream(file);
                        out.writeObject(populations.get(i).getAgents()[j].getNet());
                        out.close();
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultCells.add(new Number(0, index, i + 1));
                    resultCells.add(new Number(1, index, j + 1));
                    resultCells.add(new Number(2, index, fitness));
                    resultCells.add(new Number(3, index, populations.get(i).getAgents()[j].getSeed()));
                    index++;
                }
            }

            for (WritableCell writableCell : resultCells) {
                resultSheet.addCell(writableCell);
            }


            WritableSheet configSheet = workbook.createSheet("Config", 1);
            ArrayList<WritableCell> configCells = new ArrayList<>();
            if (neuralConfig != null) {
                configCells.add(new Label(0, 0, "Activation function:"));
                configCells.add(new Label(0, 1, "Topology:"));
                configCells.add(new Label(1, 0, String.valueOf(neuralConfig.getActivation())));
                configCells.add(new Label(1, 1, String.valueOf(neuralConfig.getTopologyString())));
            }

            if (gameConfig != null) {
                configCells.add(new Label(0, 2, "Map:"));
                configCells.add(new Label(1, 2, String.valueOf(gameConfig.getMapName())));
            }

            if (evolutionConfig != null) {
                configCells.add(new Label(0, 3, "Population size:"));
                configCells.add(new Label(0, 4, "Mutation rate:"));
                configCells.add(new Label(0, 5, "Number of elites:"));
                configCells.add(new Number(1, 3, evolutionConfig.getPopulationSize()));
                configCells.add(new Number(1, 4, evolutionConfig.getMutationRate()));
                configCells.add(new Number(1, 5, evolutionConfig.getNumberOfElites()));
            }

            for (WritableCell configCell : configCells) {
                configSheet.addCell(configCell);
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String uniqueFileName() {
        for (int i = 1; i > 0; i++) {
            String fileName = "results/result" + i + ".xls";
            if (!new File(fileName).exists())
                return fileName;
        }
        return "results/result.xls";
    }

}

