package evolution;

import neuralNetwork.NeuralConfig;
import neuralNetwork.NeuralNet;

import java.util.Random;

public class Agent implements Comparable {
    private NeuralNet net; //The neural network
    private double fitness = 0;
    private boolean evaluated = false;
    private long seed;

    /**
     * Creates an agent with random net.
     *
     * @param neuralConfig neural network parameters.
     */
    Agent(NeuralConfig neuralConfig) {
        net = new NeuralNet(neuralConfig.getTopology(), neuralConfig.getActivation());
        net.randomize(neuralConfig.getLowerBound(), neuralConfig.getUpperBound());
        Random r = new Random();
        seed = r.nextLong();
    }

    Agent(NeuralNet neuralNet) {
        this.net = neuralNet;
        Random r = new Random();
        seed = r.nextLong();
    }

    public Agent(NeuralNet neuralNet, long seed){
        this.net = neuralNet;
        this.seed = seed;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.fitness, ((Agent) o).fitness);
    }

    public NeuralNet getNet() {
        return net;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public long getSeed() {
        return seed;
    }

    boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

}