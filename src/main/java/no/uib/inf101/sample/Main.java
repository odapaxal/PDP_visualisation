package no.uib.inf101.sample;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.solver.ALNS;
import no.uib.inf101.sample.utils.VehicleRouteFormatter;
import no.uib.inf101.sample.utils.calculations.ObjectiveFunction;

/**
 * Hello world!
 */
public class Main {

	public static void main(String[] args) throws IOException {
		Read read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
		Main main = new Main();
		HashMap<Integer, List<Integer>> solution = main.run(read);
		System.out.println("Best solution: " + solution);
		
	}

	public HashMap<Integer, List<Integer>> run(Read read) throws IOException {
		long startTime = System.nanoTime();

		ObjectiveFunction objectiveFunction = new ObjectiveFunction(read);
		VehicleRouteFormatter format = new VehicleRouteFormatter();
		ALNS solver = new ALNS(read);
		List<Integer> initalSolution = new ArrayList<>(Arrays.asList(0,0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7));
		HashMap<Integer, List<Integer>> initialSolution = format.fromListToHashMap(initalSolution);
		HashMap<Integer, List<Integer>> solution = new HashMap<>();

		int initalObjectiveValue = objectiveFunction.calculateTotal(initialSolution);
		solver.alns(initalObjectiveValue, initialSolution);
		solution = solver.getBestSolution();
		int bestObjectiveValue = solver.getObjectiveValue();
		System.out.println("Best objective value: " + bestObjectiveValue);

		long endTime = System.nanoTime();
		double executionTime = (endTime - startTime) / 1_000_000_000.0;

		return solution;
	}
}
