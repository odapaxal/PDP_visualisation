package no.uib.inf101.sample.operators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.operators.insert.InsertOperator;
import no.uib.inf101.sample.operators.removal.RemovalOperator;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.solver.ALNS;
import no.uib.inf101.sample.utils.calculations.CalculateCost;



public class Operator {
    Read read;
    CalculateCost calculateCost;

    public Operator(Read read) {
        this.read = read;
        this.calculateCost = new CalculateCost(read);
    }
   

    /**
     * Changes the solution by removing, reinserting calls
     * @param solution
     * @return altered solution
     * @throws IOException
     */
    public HashMap<Integer, List<Integer>> operate(HashMap<Integer, List<Integer>> newSolution, 
                                                    RemovalOperator removalOperator, 
                                                    InsertOperator insertOperator, int k) throws IOException{
        // Make a deep copy of the solution
        HashMap<Integer, List<Integer>> solution = ALNS.deepCopy(newSolution);
        HashMap<Integer, List<Integer>> previousSolution = new HashMap<>(solution);

        List<Integer> callsToRemove = removalOperator.chooseCalls(solution, k);
        int vehicleToRemove = -1;
        
        // Remove calls from the chosen vehicle
        for (int call : callsToRemove) {
            vehicleToRemove = solution.entrySet().stream()
                .filter(entry -> entry.getValue().contains(call))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(-1);
            if (vehicleToRemove == -1) {
                throw new IllegalArgumentException("Call " + call + " not found in any vehicle.");
            }
            List<Integer> targetList = new ArrayList<>(solution.get(vehicleToRemove));
            // Remove the calls from their vehicle
            while (targetList.contains(call)) {
                targetList.remove(targetList.indexOf(call));
            }
            solution.put(vehicleToRemove, new ArrayList<>(targetList));
        }
        
        // Reinsert calls
        solution = new HashMap<>(insertOperator.operate(solution, callsToRemove, previousSolution));

        return solution;
    }; 
    
}
