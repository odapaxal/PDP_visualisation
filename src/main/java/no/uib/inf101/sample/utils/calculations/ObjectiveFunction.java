package no.uib.inf101.sample.utils.calculations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.NodeInfo;
import no.uib.inf101.sample.objects.NodeKey;
import no.uib.inf101.sample.parser.Read;


public class ObjectiveFunction {
    Read read;

    public ObjectiveFunction(Read read){
        this.read = read;
    }

    /**
     * Calculate the total cost of the solution
     * @param solution
     * @return int totalcost of solution
     * @throws IOException
     */
    public int calculateTotal(Map<Integer, List<Integer>> solution) throws IOException{
        int totalCost = 0;
        for (Map.Entry<Integer, List<Integer>> entry : solution.entrySet()) {
            int vehicleId = entry.getKey();
            List<Integer> assignedCalls = entry.getValue();
            if (assignedCalls.isEmpty()){continue;} // Skip if no calls assigned to the vehicle
            totalCost += calculateObjectiveFunction(vehicleId, assignedCalls);
        }
        return totalCost;
    }

    /**
     * Calculate the objective function for a single vehicle
     * @param vehicleId
     * @param assignedCalls
     * @return int totalCost for the vehicle
     * @throws IOException
     */
    public int calculateObjectiveFunction(int vehicleId, List<Integer> assignedCalls) throws IOException {
        List<Call> calls = read.getCalls();
        HashMap<NodeKey, NodeInfo> nodeHashMap = read.getNodeMap();
        CalculateCost myCalculateCost  = new CalculateCost(read);
        int totalCost = 0;

        // Compute transportation costs
        if (vehicleId != 0) {
            int currentTravelcost = myCalculateCost.calculateCost(vehicleId, assignedCalls);
            totalCost += currentTravelcost;
            
            // Cost of nodes
            int portCost = 0;
            for (int callId : assignedCalls) {
                int originNodeCost = 0;
                int destNodeCost = 0;

                NodeKey nkey = new NodeKey(vehicleId, callId);
                originNodeCost = nodeHashMap.get(nkey).getOriginNodeCost();
                destNodeCost = nodeHashMap.get(nkey).getDestNodeCost();

                portCost += originNodeCost + destNodeCost;
                
            }
            totalCost += (portCost/2);
        } else {
        // Compute penalty costs for unassigned calls
        int penalty = 0;
        for (int call : assignedCalls){
            penalty += calls.get(call-1).getCostOfNotTransporting();
        }
        totalCost += (penalty/2); // because each call is added twice
        }
    
        return totalCost;
    }
}

    
