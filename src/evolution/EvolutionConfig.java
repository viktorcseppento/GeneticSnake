package evolution;

import java.util.prefs.Preferences;

public class EvolutionConfig {
    private int populationSize;
    private double mutationRate;
    private int numberOfElites;

    public EvolutionConfig() {
        Preferences evolutionPref = Preferences.userNodeForPackage(this.getClass());
        this.populationSize = evolutionPref.getInt("populationSize", 20);
        this.mutationRate = evolutionPref.getDouble("mutationRate", 0.01);
        this.numberOfElites = evolutionPref.getInt("numberOfElites", 2);
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getNumberOfElites() {
        return numberOfElites;
    }
}
