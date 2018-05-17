package evolution;

import neuralNetwork.NeuralConfig;
import java.util.Random;

public class Population {
    private Agent[] agents;

    private Population(EvolutionConfig evolutionConfig) {
        agents = new Agent[evolutionConfig.getPopulationSize()];
    }

    public static Population createRandomGeneration(EvolutionConfig evolutionConfig, NeuralConfig neuralConfig) {
        Population newPop = new Population(evolutionConfig);
        for (int i = 0; i < newPop.agents.length; i++) {
            newPop.agents[i] = new Agent(neuralConfig);
        }

        return newPop;
    }

    public Population createNewGeneration(EvolutionConfig evolutionConfig, NeuralConfig neuralConfig) {
        Population newPop = new Population(evolutionConfig);
        double mutationRate = evolutionConfig.getMutationRate();
        int numberOfElites = evolutionConfig.getNumberOfElites();
        double lowerBound = neuralConfig.getLowerBound();
        double upperBound = neuralConfig.getUpperBound();
        for (int i = 0; i < numberOfElites; i++) {
            newPop.agents[i] = new Agent(this.agents[i].getNet());
        }
        for (int i = numberOfElites; i < agents.length; i++) {
            Agent parentA = selection();
            Agent parentB = selection();
            newPop.agents[i] = new Agent(parentA.getNet().breed(parentB.getNet(), mutationRate, lowerBound, upperBound, neuralConfig.getActivation()));
        }

        return newPop;
    }

    private Agent selection() {
        Random random = new Random();
        double sumFitness = 0;
        for (Agent a : agents)
            sumFitness += a.getFitness();

        double rand = random.nextDouble() * sumFitness;

        for (Agent a : agents) {
            rand -= a.getFitness();
            if (rand <= 0)
                return a;
        }

        return agents[agents.length - 1];
    }

    public boolean evaluated() {
        for (Agent a : agents)
            if (!a.isEvaluated())
                return false;
        return true;
    }

    public Agent[] getAgents() {
        return agents;
    }
}
