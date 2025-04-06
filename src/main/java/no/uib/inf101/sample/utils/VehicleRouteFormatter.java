package no.uib.inf101.sample.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VehicleRouteFormatter {
    public List<Integer> formatSolution(HashMap<Integer, List<Integer>> solution) {
        List<Integer> result = new ArrayList<>();
        List<Integer> dummyVehicleNodes = new ArrayList<>();

        // Extract the keys and sort them to process normal vehicles first
        List<Integer> vehicleKeys = new ArrayList<>(solution.keySet());
        Collections.sort(vehicleKeys);

        for (Integer vehicle : vehicleKeys) {
            List<Integer> nodes = solution.get(vehicle);
            if (vehicle == 0) {
                // Store dummy vehicle's nodes to append later
                dummyVehicleNodes.addAll(nodes);
            } else {
                result.addAll(nodes);
                result.add(0); // Separator
            }
        }

        // Append dummy vehicle nodes at the end if any
        if (!dummyVehicleNodes.isEmpty()) {
            result.addAll(dummyVehicleNodes);
        }

        return result;
}
    public HashMap<Integer, List<Integer>> fromListToHashMap(List<Integer> solution){
        HashMap<Integer, List<Integer>> assignedCalls = new HashMap<>();
        int currentVehicle = 1;
        for (int call : solution) {
            assignedCalls.putIfAbsent(currentVehicle, new ArrayList<>());
            if (call == 0) {
                currentVehicle++;
                continue;
            }
            assignedCalls.putIfAbsent(currentVehicle, new ArrayList<>());
            assignedCalls.get(currentVehicle).add(call);
        }
        
        // Assign calls after the last 0 to vehicle 0
        if (assignedCalls.containsKey(currentVehicle)) {
            assignedCalls.put(0, assignedCalls.get(currentVehicle));
            assignedCalls.remove(currentVehicle);
        } else {
            assignedCalls.putIfAbsent(0, new ArrayList<>());
        }
        
        return assignedCalls;
    }
}
