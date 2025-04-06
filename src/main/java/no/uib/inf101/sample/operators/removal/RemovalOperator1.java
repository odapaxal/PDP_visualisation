package no.uib.inf101.sample.operators.removal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.utils.calculations.CalculateCost;
import no.uib.inf101.sample.utils.calculations.WeightedRandomiser;


// Remove a set of expensive calls from expensive vehicle 
public class RemovalOperator1 extends RemovalOperator {
    Read read;
    CalculateCost calculateCost;

    public RemovalOperator1(Read read) {
        super(read);
        this.read = read;
        calculateCost = new CalculateCost(read);
    }

    @Override
    /**
     * Choose calls to remove from a vehicle based on cost-weighted randomisation
     * @param vehicle
     * @param list of assigned calls to vehicle 
     * @return list of calls to remove
     * @throws IOException
     */
    public List<Integer> chooseCalls(HashMap<Integer, List<Integer>> solution, int k) throws IOException {
        List<Integer> callsToRemove = new ArrayList<>();

        // Calculate cost of each call
        HashMap<Integer, Integer> callCosts = calculateCost.calculateTotalCost(solution);

        int totalCost = callCosts.get(-1);
        callCosts.remove(-1); // Remove the total cost entry

        // If less than k calls available, remove all
        if (callCosts.size() < k) {
            callsToRemove.addAll(callCosts.keySet());
            return callsToRemove;
        }

        // Make weight map according to costs
        HashMap<Integer, Integer> weights = new HashMap<>();
        for (var entry : callCosts.entrySet()) {
            int call = entry.getKey();
            int cost = entry.getValue();
            if (cost <= 0) {
                continue;
            }
            double percentage = (double) cost / totalCost;
            int weight = (int) Math.round(percentage * 100);
            if (weight == 0) {
                continue;
            }
            weights.put(call, weight);
        }

        // Check if there are enough unique calls to remove
        if (weights.size() < k) {
            callsToRemove.addAll(weights.keySet());
            return callsToRemove;
        }

        // Use WeightedRandomiser to select calls
        WeightedRandomiser weightedRandomiser = new WeightedRandomiser(weights);
        while (callsToRemove.size() < k) {
            int call = weightedRandomiser.next();
            if (!callsToRemove.contains(call)) {
                callsToRemove.add(call);
            }
        }
        return callsToRemove;
    }    
}
