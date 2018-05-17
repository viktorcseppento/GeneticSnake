package neuralNetwork;

import static java.lang.Math.exp;
import static java.lang.Math.tanh;

public class ActivationFunctions {


    public static double applyActivation(double param, String activation, boolean derivative) throws RuntimeException {
        switch (activation) {
            case "SIGMOID":
                return derivative ? sigmoidDerivative(param) : sigmoid(param);

            case "TANH":
                return derivative ? tanHDerivative(param) : tanh(param);

            case "RELU":
                return derivative ? ReLuDerivative(param) : ReLu(param);
        }

        throw new RuntimeException("No such activation function");
    }

    /**
     * Sigmoid activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public static double sigmoid(double param) {
        return 1 / (1 + exp(-param));
    }

    /**
     * Derivative of sigmoid activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public static double sigmoidDerivative(double param) {
        return exp(param) / ((1 + exp(param)) * (1 + exp(param)));
    }

    /**
     * ReLu activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public static double ReLu(double param) {
        return param > 0 ? param : 0;
    }

    /**
     * Derivative of ReLu activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public static double ReLuDerivative(double param) {
        return param > 0 ? 1 : 0;
    }

    /**
     * Tanh activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public double tanH(double param) {
        return tanh(param);
    }

    /**
     * Derivative of tanh activation function.
     *
     * @param param double parameter.
     * @return result.
     */
    public static double tanHDerivative(double param) {
        return 1 - tanh(param) * tanh(param);
    }
}
