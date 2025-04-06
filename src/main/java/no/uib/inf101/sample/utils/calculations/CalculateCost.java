package no.uib.inf101.sample.utils.calculations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.objects.Vehicle;
import no.uib.inf101.sample.parser.Read;


public class CalculateCost {
    Read read;
    int maxCost;
    int currentCost;
    List<Call> calls;
    List<Vehicle> vehicles;
    HashMap<Integer,Integer> callCost;
    HashMap<Integer,Integer> callAndVehicle;
    HashMap<Integer, List<Integer>> bestSolution; 

    public CalculateCost(Read read){
        this.read = read;
        maxCost = -1;
        currentCost = -1;
        calls = read.getCalls();
        vehicles = read.getVehicles();
    }

    /**
     * Calculates the total cost of a solution and cost for each call
     * @param solution
     * @return HashMap with the total cost at index -1 and cost for each call
     * @throws IOException
     */
    public HashMap<Integer, Integer> calculateTotalCost(HashMap<Integer, List<Integer>> solution) throws IOException{
        HashMap<Integer, Integer> totalCallCosts = new HashMap<>();
        int totalCost = 0;
        for (var entry: solution.entrySet()){
            totalCost += calculateCost(entry.getKey(), entry.getValue());
            totalCallCosts.put(-1, totalCost);
            HashMap<Integer, Integer> callCosts = getCallCost();
            for (var callEntry: callCosts.entrySet()){
                totalCallCosts.put(callEntry.getKey(), callEntry.getValue());
            }
        }
        totalCallCosts.put(-1, totalCost);
        return totalCallCosts;
    }

    /**
     * Calculates the cost of a vehicle with a list of assigned calls
     * @param vehicleIndex
     * @param assignedCalls
     * @return totalcost
     * @throws IOException
     */
    public int calculateCost(int vehicleIndex, List<Integer> assignedCalls) throws IOException{
        HashMap<TravelKey, TravelInfo> traveHashMap = read.getTravelMap();
        int totalCost = 0;
        callCost = new HashMap<>();

         // Dummy vehicle
        if (vehicleIndex == 0) {
            for (int node : assignedCalls) {
                int cost = calls.get(node - 1).getCostOfNotTransporting();
                totalCost += cost;
                callCost.merge(node, cost, Integer::sum);
            }
            return totalCost;
        }

       
        boolean previousWasPickup = true;
        int origin = -1;
        int dest = -1;
        Vehicle vehicle = vehicles.get(vehicleIndex - 1);

        for (int i = 0; i < assignedCalls.size(); i++) {
            int callId = assignedCalls.get(i);
            if (i == 0) {
                origin = vehicle.getHomeNode();
                dest = calls.get(callId - 1).getOriginNode();
                previousWasPickup = true;
                // Get the travel cost for the first leg
                TravelKey tkey = new TravelKey(vehicleIndex, origin, dest);
                currentCost = traveHashMap.get(tkey).getTravelCost();
            } else {
                int previousCallId = assignedCalls.get(i - 1);
                if (previousWasPickup) {
                    origin = calls.get(previousCallId - 1).getOriginNode();
                } else {
                    origin = calls.get(previousCallId - 1).getDestinationNode();
                }
                if (!callCost.containsKey(calls.get(callId - 1).getIndex())) {
                    dest = calls.get(callId - 1).getOriginNode();
                    previousWasPickup = true;
                } else {
                    dest = calls.get(callId - 1).getDestinationNode();
                    previousWasPickup = false;
                }
                TravelKey tkey = new TravelKey(vehicleIndex, origin, dest);
                currentCost = traveHashMap.get(tkey).getTravelCost();
            }
            totalCost += currentCost;
            callCost.merge(callId, currentCost, Integer::sum);
        }
        return totalCost;
    }
    public HashMap<Integer, Integer> getCallCost(){return callCost;}
    public HashMap<Integer, Integer> getCallAndVehicle(){return callAndVehicle;}
    public int getMaxCost() {return maxCost;}
    public int getCurrentCost() {return currentCost;}
    public HashMap<Integer, List<Integer>> getBestSolution() {return bestSolution;}
}
