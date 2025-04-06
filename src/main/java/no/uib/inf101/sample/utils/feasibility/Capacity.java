package no.uib.inf101.sample.utils.feasibility;

import java.io.IOException;
import java.util.*;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.Vehicle;
import no.uib.inf101.sample.parser.Read;

public class Capacity {
    Read read;
    List<Vehicle> vehicles;
    List<Call> calls;

    public Capacity(Read read) {
        this.read = read;
        vehicles = read.getVehicles();
        calls = read.getCalls();
    }

    /**
     * Checking capacity for a full hashmap solution
     * @param solution
     * @return true/false feasibility
     * @throws IOException
     */
    public boolean checkCapacity(HashMap<Integer, List<Integer>> solution) throws IOException {
        // Process each vehicle's route in the solution
        for (Map.Entry<Integer, List<Integer>> entry : solution.entrySet()) {
            int vehicle = entry.getKey();
            if( vehicle < 0){
                throw new IOException("Vehicle index is negative: " + vehicle+ " for solution: " + solution);
            }

            // Skip dummy vehicle (vehicle index 0)
            if (vehicle == 0) {
                continue;
            }
            if (!checkListCapacity(vehicle, entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks capacity for single list in solution
     * @param vehicle
     * @param assignedCalls
     * @param vehicleCapacities
     * @param callSizes
     * @return
     */
    public boolean checkListCapacity(int vehicle, List<Integer> callList) {
        if(vehicle < 0){
            throw new IllegalArgumentException("Vehicle index is negative: " + vehicle+ " for callList: " + callList);
        }
        if (vehicle == 0){ //dummy is always feasible
            return true;
        }
        int maxCapacity = vehicles.get(vehicle-1).getCapacity();
        int load = 0;
        Set<Integer> pickedUpCalls = new HashSet<>();

        // Process each call in the list
        for (int call : callList) {
            int callSize = calls.get(call-1).getSize(); //double check

            if (pickedUpCalls.contains(call)) {// Delivery: reduce the load
                load -= callSize;
                pickedUpCalls.remove(call);
            } else { // Pickup: increase the load
                load += callSize;
                pickedUpCalls.add(call);
            }

            // Check if the load exceeds the vehicle's capacity
            if (load > maxCapacity) {
                //System.out.println("Capacity for vehicle " + vehicle + " is exceeded: load = " + load + " max = " + maxCapacity);
                return false;
            }
        }

        return true;
    }
}

    


