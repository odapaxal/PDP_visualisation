package no.uib.inf101.sample.operators.insert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf101.sample.operators.Operator;
import no.uib.inf101.sample.parser.Read;

public abstract class InsertOperator extends Operator{
    Read read;
    Map<Integer, List<Integer>> vehicleCalls;

    public InsertOperator(Read read) {
        super(read);
        this.read = read;
        vehicleCalls = read.getVehicleCalls();
    }
    
    /**
     * Insert the calls into the solution
     * @param newSolution
     * @param callsToInsert
     * @param previousVehicle
     * @return new solution
     * @throws IOException
     */
    public abstract HashMap<Integer, List<Integer>> operate(HashMap<Integer, List<Integer>> newSolution, 
                                                            List<Integer> callsToInsert, 
                                                            HashMap<Integer, List<Integer>> previousSolution) throws IOException;

    /**
     * return a hashmap of the solution, where the vehicles that are incompatible have been removed
     * dummy is not in vehicleCalls, and therefore always allowed
     * @param solution
     * @param targetCall
     * @return
     */
    public HashMap<Integer,List<Integer>> removeIncompatibleVehicles(HashMap<Integer,List<Integer>> solution, int target){
        HashMap<Integer, List<Integer>> solutionCopy = new HashMap<>(solution);
        for (Map.Entry<Integer, List<Integer>> entry : vehicleCalls.entrySet()){
            int currentVehicle = entry.getKey();
            List<Integer> possibleCalls = entry.getValue();

            if (!possibleCalls.contains(target)){
                solutionCopy.remove(currentVehicle);
            }
        }
        return solutionCopy;
    }
    

}
