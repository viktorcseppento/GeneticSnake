package neuralNetwork;

import java.io.Serializable;

public class Layer implements Serializable{

    private Layer previous;
    private Matrix output;
    private Matrix weights;
    private Matrix biases;
    private String activation;

    /**
     * Creates a layer with a given size gets the previous layer and creates the parameter matrices full of 0s.
     *
     * @param previous previous layer, null if input layer.
     * @param size     number of outputs.
     */
    Layer(Layer previous, int size, String activation) {
        this.previous = previous;
        this.output = new Matrix(size, 1);
        if (previous == null) {
            weights = new Matrix(0, 0);
            biases = new Matrix(0, 0);
        } else {
            weights = new Matrix(size, previous.output.getRows());
            biases = new Matrix(size, 1);
        }

        this.activation = activation;
    }

    /**
     * Gives the output if the inputs are the outputs of the previous layer. Does nothing for input layer.
     */
    void feedForward() {
        if (previous != null) {
            output = weights.multiply(previous.output).add(biases);
            applyActivation(output);
        }
    }

    /**
     * Applies the activation function to the matrix.
     *
     * @param matrix input matrix.
     */
    private void applyActivation(Matrix matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                matrix.getData()[i][j] = ActivationFunctions.applyActivation(matrix.getData()[i][j], activation, false);
            }
        }
    }

    public Layer getPrevious() {
        return previous;
    }

    public Matrix getOutput() {
        return output;
    }

    public Matrix getWeights() {
        return weights;
    }

    public Matrix getBiases() {
        return biases;
    }

    public String getActivation() {
        return activation;
    }

    public void setOutput(Matrix output) {
        this.output = output;
    }

    public void setWeights(Matrix weights) {
        this.weights = weights;
    }

    public void setBiases(Matrix biases) {
        this.biases = biases;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "weights=" + weights +
                ", biases=" + biases +
                '}';
    }
}
