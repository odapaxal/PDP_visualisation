package no.uib.inf101.sample.utils.feasibility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.objects.NodeInfo;
import no.uib.inf101.sample.objects.NodeKey;
import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.parser.Read;


public class CheckTimeWindow {
    Read read;
    boolean feasible;
    int destinationNode;
    int originNode;
    int lowerTimeWindow;
    int upperTimeWindow;
    int loadingTime;
    boolean firstCallVisited;
    private HashMap<Integer, Integer> tightnessVehicleMap; // what happens if empty list 
    private HashMap<Integer, Integer> tightnessCallMap;

    public CheckTimeWindow(Read read){
        this.read = read;
    }

    /**
     * Check time window feasibility of a solution
     * @param Hashmap solution
     * @return boolean feasible
     */
    public boolean checkAllTimeWindows(HashMap<Integer, List<Integer>> solution){
        tightnessVehicleMap = new HashMap<>();

        for(var entry : solution.entrySet()){
            int vehicle = entry.getKey();
            List<Integer> route = entry.getValue();
            if (vehicle == 0 || route.isEmpty()) { // dummy vehicle or empty route
                tightnessCallMap = new HashMap<>();
                continue;
            }
            feasible = checkTimeWindow(vehicle, route);
            int totalTightness = tightnessCallMap.values().stream().mapToInt(Integer::intValue).sum();
            tightnessVehicleMap.put(vehicle, totalTightness);
            if (!feasible){
                return false;
            }
        }
        return true;
    }

    /**
     * Check time window feasibility of a route
     * @param vehicle
     * @param route
     * @return boolean feasible
     */
    public boolean checkTimeWindow(int vehicle, List<Integer> route){
        tightnessCallMap = new HashMap<>();
        int tightness = 0; //waiting time 
        if (vehicle == 0 || route.isEmpty()) { // dummy vehicle or empty route
            if (vehicle == 0){ //if dummy set equal tightness
                for (int call : route){
                    tightnessCallMap.merge(call,1, Integer::sum);
                }}
            return true;
        }
        HashMap<TravelKey, TravelInfo> travelTimeMap = read.getTravelMap();
        HashMap<NodeKey, NodeInfo> nodeHashMap = read.getNodeMap();
        List<Integer> visited = new ArrayList<>();
        int currentTime = read.getVehicles().get(vehicle - 1).getStartTime();

        // Check if the vehicle arrives at the first call within the pickup time window
        int firstCallIndex = route.get(0) - 1;
        int homeNode = read.getVehicles().get(vehicle - 1).getHomeNode();
        originNode = read.getCalls().get(firstCallIndex).getOriginNode();
        TravelKey tkey = new TravelKey(vehicle, homeNode, originNode);
        int travelTime = travelTimeMap.get(tkey).getTravelTime();

        currentTime += travelTime;
        upperTimeWindow = read.getCalls().get(firstCallIndex).getPickupUpperTimeWindow();
        lowerTimeWindow = read.getCalls().get(firstCallIndex).getPickupLowerTimeWindow();
        if (currentTime > upperTimeWindow) {
            System.out.println("First call out of time window");
            return false;
        } else {
            currentTime = Math.max(travelTime, lowerTimeWindow); // wait if necessary
            NodeKey nkey = new NodeKey(vehicle, firstCallIndex + 1);
            currentTime +=  nodeHashMap.get(nkey).getOriginNodeTime(); // add loading time
            visited.add(firstCallIndex + 1);
            firstCallVisited = true;

        }

        //proceed for next calls
        for (int call : route){
            if (firstCallVisited){ //first pickup has already been processed
                firstCallVisited = false;
                continue;
            }

            NodeKey nkey = new NodeKey(vehicle, call);
            if(visited.contains(call)){ // if delivery
                destinationNode = read.getCalls().get(call - 1).getDestinationNode();
                lowerTimeWindow = read.getCalls().get(call - 1).getDeliveryLowerTimeWindow();
                upperTimeWindow = read.getCalls().get(call - 1).getDeliveryUpperTimeWindow();
                loadingTime = nodeHashMap.get(nkey).getDestNodeTime();
                
            } else { // if pickup
                destinationNode = read.getCalls().get(call - 1).getOriginNode();
                lowerTimeWindow = read.getCalls().get(call - 1).getPickupLowerTimeWindow();
                upperTimeWindow = read.getCalls().get(call - 1).getPickupUpperTimeWindow();
                loadingTime = nodeHashMap.get(nkey).getOriginNodeTime();
            }

            // Travel from origin to destination
            TravelKey tkey2 = new TravelKey(vehicle, originNode, destinationNode);
            travelTime = travelTimeMap.get(tkey2).getTravelTime();
            currentTime += travelTime;

            // Check if the vehicle arrives within the time window
            if (currentTime > upperTimeWindow) {
                //System.out.println("Call " + call + " out of time window for vehicle " + vehicle);
                return false;
            } else {
                if (currentTime < lowerTimeWindow) {
                    int wait = lowerTimeWindow - currentTime;
                    tightness += wait;
                    currentTime = lowerTimeWindow;
                }
                currentTime += loadingTime;
                visited.add(call);
            }
            originNode = destinationNode;
            if (tightness > 0){
                tightnessCallMap.merge(call,tightness, Integer::sum);
            }
        }

        return true;
    }

    public HashMap<Integer, Integer> getTightnessVehicleMap() {return tightnessVehicleMap;}
    public HashMap<Integer, Integer> getTightnessCallMap() {return tightnessCallMap;}
}

