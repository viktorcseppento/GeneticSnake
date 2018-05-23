package neuralNetwork;

import java.util.Arrays;
import java.util.Random;

public class NeuralNet implements java.io.Serializable{
    private Layer[] layers;
    private int[] topology; //[Number of input neurons, neurons in first hidden layer, ..., number of output neurons]

    /**
     * Creates a neural net full of 0s with the appropriate layer sizes .
     *
     * @param topology   number of neurons in each layer.
     * @param activation activation function.
     */
    public NeuralNet(int[] topology, String activation) {
        this.topology = topology;

        layers = new Layer[topology.length];
        layers[0] = new Layer(null, topology[0], null);
        for (int i = 1; i < topology.length; i++) {
            layers[i] = new Layer(layers[i - 1], topology[i], activation);
        }
    }

    /**
     * Crosses over a neural net with another and mutates the child.
     * Doesn't check topologies because of performance reasons.
     *
     * @param other        other neural net.
     * @param mutationRate probability of mutating each gene.
     * @param lowerBound   lower bound of the random genes.
     * @param upperBound   upper bound of the random genes.
     * @param activation   activation function.
     * @return child neural net.
     */
    public NeuralNet breed(NeuralNet other, double mutationRate, double lowerBound, double upperBound, String activation) {
        NeuralNet child = new NeuralNet(this.topology, activation);
        double chanceFromOneParent = 0.5;
        Random r = new Random();
        //Go through each layer
        for (int i = 1; i < layers.length; i++) {
            Matrix weights = new Matrix(topology[i], topology[i - 1]);
            Matrix biases = new Matrix(topology[i], 1);

            //Sets the weight matrix
            for (int j = 0; j < weights.getRows(); j++) {
                for (int k = 0; k < weights.getCols(); k++) {
                    double randomDouble = r.nextDouble();
                    if (randomDouble < chanceFromOneParent) //If gets from the first parent
                        weights.set(j, k, this.layers[i].getWeights().get(j, k));
                    else //If gets from the second parent
                        weights.set(j, k, other.layers[i].getWeights().get(j, k));

                }
            }

            //Sets the bias vector
            for (int j = 0; j < layers[i].getBiases().getRows(); j++) {
                double randomDouble = r.nextDouble();
                if (randomDouble < chanceFromOneParent) //If gets from the first parent
                    biases.set(j, 0, this.layers[i].getBiases().get(j, 0));
                else//If gets from the second parent
                    biases.set(j, 0, other.layers[i].getBiases().get(j, 0));
            }

            child.mutate(mutationRate, lowerBound, upperBound);
            child.layers[i].setWeights(weights);
            child.layers[i].setBiases(biases);
        }

        return child;
    }

    /**
     * Mutates each parameter with a probability.
     *
     * @param mutationRate chance to mutate one parameter.
     * @param lowerBound   lower bound.
     * @param upperBound   upper bound.
     */
    public void mutate(double mutationRate, double lowerBound, double upperBound) {
        Random random = new Random();
        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].getWeights().getRows(); j++) {
                for (int k = 0; k < layers[i].getWeights().getCols(); k++) {
                    if (random.nextDouble() < mutationRate)
                        layers[i].getWeights().set(j, k, random.nextDouble() * (upperBound - lowerBound) + lowerBound);
                }
            }

            for (int j = 0; j < layers[i].getBiases().getRows(); j++) {
                if (random.nextDouble() < mutationRate)
                    layers[i].getBiases().set(j, 0, random.nextDouble() * (upperBound - lowerBound) + lowerBound);
            }
        }
    }

    /**
     * Sets the coefficients of the neural network to random values between the bounds.
     *
     * @param lowerBound lower bound.
     * @param upperBound upper bound.
     */
    public void randomize(double lowerBound, double upperBound) {
        for (int i = 1; i < layers.length; i++) {
            layers[i].getWeights().randomize(lowerBound, upperBound);
            layers[i].getBiases().randomize(lowerBound, upperBound);
        }
    }

    /**
     * Evaluates the neural network with the given inputs.
     *
     * @param inputs inputs of the network.
     * @return output of the last layer.
     */
    public Matrix feedForward(Matrix inputs) {
        layers[0].setOutput(inputs);
        for (int i = 1; i < layers.length; i++) {
            layers[i].feedForward();
        }
        return layers[layers.length - 1].getOutput();
    }

    /**
     * Returns the number of parameters in the network.
     *
     * @return number of weights and biases.
     */
    public int getNumberOfCoeffs() {
        int sum = 0;

        for (Layer l : layers) {
            sum += l.getWeights().getRows();
            sum += l.getBiases().getRows();
        }

        return sum;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public int[] getTopology() {
        return topology;
    }

    @Override
    public String toString() {
        return "NeuralNet{" +
                "layers=" + Arrays.toString(layers) +
                ", topology=" + Arrays.toString(topology) +
                '}';
    }
}
