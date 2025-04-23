package no.uib.inf101.sample.visualisation.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.solver.ALNS;
import no.uib.inf101.sample.utils.VehicleRouteFormatter;
import no.uib.inf101.sample.utils.calculations.ObjectiveFunction;

public class Solution {
    private HashMap<Integer, List<Integer>> solution;
    private int objectiveValue;
    private double executionTime;
    Read read;

    /**
     * Constructor for Solution
     * @param read data to be used
     */
    public Solution(Read read){
        this.read = read;
    }

    /**
     * Run ALNS algoritm 
     * Setting solution, objectiveValue and executionTime
     * @throws IOException
     */
    public void run() throws IOException {
		long startTime = System.nanoTime();
        System.out.println("Running ALNS algorithm...");

		ObjectiveFunction objectiveFunction = new ObjectiveFunction(read);
		VehicleRouteFormatter format = new VehicleRouteFormatter();
		ALNS solver = new ALNS(read);
		List<Integer> initalSolution = new ArrayList<>(Arrays.asList(0,0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7));
		HashMap<Integer, List<Integer>> initialSolution = format.fromListToHashMap(initalSolution);
		solution = new HashMap<>();

		int initalObjectiveValue = objectiveFunction.calculateTotal(initialSolution);
		solver.alns(initalObjectiveValue, initialSolution);
		solution = solver.getBestSolution();
		objectiveValue = solver.getObjectiveValue();

		long endTime = System.nanoTime();
		executionTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("ALNS algorithm completed, solution: " + solution);
	}
    
    public HashMap<Integer, List<Integer>> getSolution() {
        return solution;
    }
    public int getObjectiveValue() {
        return objectiveValue;
    }
    public double getExecutionTime() {
        return executionTime;
    }
}
