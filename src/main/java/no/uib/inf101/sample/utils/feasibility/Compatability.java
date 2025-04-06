package no.uib.inf101.sample.utils.feasibility;

import java.io.IOException;
import java.util.*;

import no.uib.inf101.sample.parser.Read;

public class Compatability {
    Read read;
    Map<Integer, List<Integer>> vehicleCalls;

    public Compatability(Read read){
        this.read = read;
        vehicleCalls = read.getVehicleCalls();
    }

    public boolean checkCompatability(HashMap<Integer, List<Integer>> solutionIndexes) throws IOException {

        for (Map.Entry<Integer, List<Integer>> entry : solutionIndexes.entrySet()) {
            int vehicle = entry.getKey();

            if (vehicle == 0) {
                continue; // Skip dummy vehicle
            }

            List<Integer> assignedCalls = entry.getValue();
            List<Integer> allowedCalls = vehicleCalls.get(vehicle);

            // If vehicle is not in the file, it's not compatible
            if (allowedCalls == null) {
                return false;
            }

            for (int call : assignedCalls) {
                if (!allowedCalls.contains(call)) {
                    //System.out.println("vehichle"+vehicle+"cannot do call "+call);
                    return false; // Return immediately if a call is not allowed
                }
            }
        }
        return true;
    }
    public HashMap<Integer, List<Integer>> removeDuplicates(HashMap<Integer, List<Integer>> solution){
            HashMap<Integer, List<Integer>> noDuplicates = new HashMap<>();
            for (Map.Entry<Integer, List<Integer>> entry : solution.entrySet()){
                Set<Integer> uniqueIntegers = new HashSet<>(entry.getValue());
                List<Integer> result = new ArrayList<>(uniqueIntegers);
                noDuplicates.put(entry.getKey(), result);
            }
            return noDuplicates;
        }
}
