package QLearning;

import neuralNetwork.NeuralConfig;
import neuralNetwork.NeuralNet;


public class QAgent {
    private NeuralNet net;


    public QAgent(NeuralNet net) {
        this.net = net;
    }

    /**
     * Creates an agent with random net.
     *
     * @param neuralConfig neural network parameters.
     */
    QAgent(NeuralConfig neuralConfig) {
        net = new NeuralNet(neuralConfig.getTopology(), neuralConfig.getActivation());
        net.randomize(neuralConfig.getLowerBound(), neuralConfig.getUpperBound());
    }
}
