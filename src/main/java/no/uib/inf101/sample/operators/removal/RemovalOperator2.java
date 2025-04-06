package no.uib.inf101.sample.operators.removal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.utils.calculations.WeightedRandomiser;
import no.uib.inf101.sample.utils.feasibility.CheckTimeWindow;


public class RemovalOperator2 extends RemovalOperator {
    Read read;
    CheckTimeWindow checkTimeWindow;
    
    public RemovalOperator2(Read read) {
        super(read);
        this.read = read;
        checkTimeWindow = new CheckTimeWindow(read);
    }

    @Override
    /**
     * Choose k calls to be removed based on tightness, with highest tightness value having largest probability
     * @param vehicleToRemove
     * @param assignedCalls
     * @return list of calls to remove
     */
    public List<Integer> chooseCalls(HashMap<Integer,List<Integer>> solution, int k) {
        int solutionSize = solution.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum();
        List<Integer> availableCalls = new ArrayList<>(new HashSet<>(solution.values().stream().flatMap(List::stream).toList()));
        List<Integer> callsToRemove = new ArrayList<>();
        HashMap<Integer, Integer> callTightnessMap = new HashMap<>();

        //if list contains less calls than k, remove all calls
        if (solutionSize/2 < k){
            callsToRemove.addAll(availableCalls);
            return callsToRemove;    
        }
        checkTimeWindow.checkAllTimeWindows(solution);
        callTightnessMap = checkTimeWindow.getTightnessCallMap();
        if(callTightnessMap.isEmpty()){ //if infeasible time windows or no waiting time, choose random calls
            for (int call : availableCalls){
                callTightnessMap.put(call, 1);
            }
        }
        //call with most waiting time will have higher probability
        HashMap<Integer, Integer> weightMap = new HashMap<>();
        //create weightmap based on tightness
        for (var entry : solution.entrySet()){
            if (entry.getValue().isEmpty()){
                continue;
            }
            weightMap.putAll(createWeightMap(callTightnessMap, entry.getKey(),solution));
        }

        //if list contains less calls than k, remove all calls
        if (weightMap.size() < k){
            callsToRemove.addAll(availableCalls);
            return callsToRemove; 
        }

        while (callsToRemove.size()<k){ 
            WeightedRandomiser weightedRandomiser = new WeightedRandomiser(weightMap);
            int targetCall = weightedRandomiser.next();
            callsToRemove.add(targetCall);
            weightMap.remove(targetCall);
        }
        return callsToRemove;
    }
    
    /**
     * Creates weightmap with calls in assigned calls of vehicle
     * @param map
     * @param vehicle
     * @return
     */
    public HashMap<Integer, Integer> createWeightMap (HashMap<Integer, Integer> map, int vehicle,
                                                         HashMap<Integer, List<Integer>> solution){ 
        HashMap<Integer, Integer> weightMap = new HashMap<>();
        List<Integer> assignedCalls = new ArrayList<>(new HashSet<>(solution.get(vehicle)));
        int total = map.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0){
            throw new IllegalArgumentException("total weight is 0 for map: " + map);
        }

        for (int call : assignedCalls){
            int weight = 0;
            int target = call;
            int value = map.getOrDefault(target,0);

            if (vehicle == 0){
                weightMap.put(target, 1);
                continue;
            }

            double percentage = (double) value / total;
            weight = (int) Math.round(percentage * 100);
            if (weight <= 0){
                continue;
            }
            weightMap.put(target, weight);
        }
        if (weightMap.isEmpty()){
            // put all calls in weightmap with equal probability
            for (int call : assignedCalls){
                weightMap.put(call, 1);
            }
        }
        return weightMap;
    }
}
