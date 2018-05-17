package neuralNetwork;

import java.util.StringTokenizer;
import java.util.prefs.Preferences;

public class NeuralConfig {
    private String topologyString;
    private int[] topology;
    private String activation;
    private int upperBound, lowerBound;

    public NeuralConfig(){
        Preferences pref = Preferences.userNodeForPackage(this.getClass());

        topologyString = pref.get("topology", "");
        StringTokenizer st = new StringTokenizer(topologyString, ",");
        int numberOfLayers = st.countTokens() + 2;
        topology = new int[numberOfLayers];
        for (int i = 1; i < numberOfLayers - 1; i++) {
            topology[i] = Integer.parseInt(st.nextToken());
        }
        topology[0] = 13; //13 inputs
        topology[numberOfLayers - 1] = 3; //3 outputs

        activation = pref.get("activation", "SIGMOID");

        upperBound = 1;
        lowerBound = -1;
    }

    public String getTopologyString() {
        return topologyString;
    }

    public int[] getTopology() {
        return topology;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public String getActivation() {
        return activation;
    }
}
