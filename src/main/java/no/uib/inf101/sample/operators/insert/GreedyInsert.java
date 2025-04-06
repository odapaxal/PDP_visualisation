package no.uib.inf101.sample.operators.insert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.objects.SolutionAndCost;
import no.uib.inf101.sample.objects.VehicleAndList;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.solver.ALNS;
import no.uib.inf101.sample.utils.calculations.CalculateCost;
import no.uib.inf101.sample.utils.feasibility.Capacity;
import no.uib.inf101.sample.utils.feasibility.CheckTimeWindow;
import no.uib.inf101.sample.utils.feasibility.Feasible;


// reinsert greedily
public class GreedyInsert extends InsertOperator{
    Read read;
    CalculateCost calculateCost;
    Capacity capacity;
    CheckTimeWindow checkTimeWindow;
    Feasible feasible;

    public GreedyInsert(Read read) {
        super(read);
        this.read = read;
        calculateCost = new CalculateCost(read);
        capacity = new Capacity(read);
        checkTimeWindow = new CheckTimeWindow(read);
        feasible = new Feasible(read);
    }

    @Override
    public HashMap<Integer, List<Integer>> operate(HashMap<Integer, List<Integer>> newSolution,
                                                    List<Integer> callsToInsert, HashMap<Integer,List<Integer>> previousSolution) throws IOException{
        HashMap<Integer, List<Integer>> solution = ALNS.deepCopy(newSolution);

        for (int call : callsToInsert) {
            int totalCalls = solution.values().stream().mapToInt(List::size).sum();
            
            VehicleAndList operated = greedyInsert(call, solution, previousSolution);
            int vehicleToInsert = operated.vehicle();
            List<Integer> assignedCalls = operated.list();
            if (assignedCalls.equals(newSolution.get(vehicleToInsert))){
                throw new IOException("Solution is the same as before, assigned calls: "+assignedCalls+", old solution: "+newSolution.get(vehicleToInsert));
            }

            solution.put(vehicleToInsert, assignedCalls);
            if (vehicleToInsert == -1) {
                throw new IOException("Failed to insert call: " + call + " because no valid vehicle was found.");
            }
            if (assignedCalls.isEmpty()) {
                throw new IOException("Failed to insert call: " + call + " because the assigned calls list is empty for vehicle: " + vehicleToInsert);
            }
            if (totalCalls + 2 != (solution.values().stream().mapToInt(List::size).sum())) {
                throw new IOException("Mismatch in total calls after insertion. Call: " + call + 
                        ", Vehicle: " + vehicleToInsert + 
                        ", Assigned Calls: " + assignedCalls + 
                        ", Total Calls Before: " + totalCalls + 
                        ", Total Calls After: " + solution.values().stream().mapToInt(List::size).sum() + 
                        ", Solution: " + solution + 
                        ", Calls To Insert: " + callsToInsert);
            }
        }
        if(!feasible.checkFeasibility(solution)){
            throw new IOException("Solution is not feasible for calls: "+callsToInsert);
        }
        return solution;
    
    }

    /**
     * Greedily insert a call into the solution
     * @param call
     * @param solution
     * @return target vehicle and its assigned calls
     * @throws IOException
     */
    public VehicleAndList greedyInsert(int call, HashMap<Integer, List<Integer>> solution, HashMap<Integer, List<Integer>> previousSolution) throws IOException {
        HashMap<Integer, List<Integer>> feasibleVehicles = removeIncompatibleVehicles(new HashMap<>(solution), call);
        List<Integer> bestSpot = new ArrayList<>();
        List<SolutionAndCost> bestSolutions = new ArrayList<>(); //if i want to keep track of all best solutions
        int bestCost = Integer.MAX_VALUE;
        int targetVehicle = -1;

        for (int vehicle : feasibleVehicles.keySet()) { //check only compatible vehicles
            int currentbestCost = Integer.MAX_VALUE;
            List<Integer> bestCurrentSpot = new ArrayList<>();
            if (vehicle == 0) continue;

            List<Integer> assignedCallsCopy = new ArrayList<>(solution.get(vehicle));
            assignedCallsCopy.add(0, call);
            assignedCallsCopy.add(0, call);

            boolean feasibleFound = capacity.checkListCapacity(vehicle, assignedCallsCopy) && checkTimeWindow.checkTimeWindow(vehicle, assignedCallsCopy);
            int currentCost = calculateCost.calculateCost(vehicle, assignedCallsCopy);
            
            if (feasibleFound){
                currentbestCost = currentCost;
                bestCurrentSpot = new ArrayList<>(assignedCallsCopy);
            }

            boolean checkedAll = false;
            boolean forward = true;
            

            while (!checkedAll){
                int index1 = assignedCallsCopy.indexOf(call);
                int index2 = assignedCallsCopy.lastIndexOf(call);

                if (index1 == (index2 - 1)){ //if calls are next to each other
                    forward = true;
                    if (index2 != assignedCallsCopy.size() -1){ //if call 2 is not last in list
                        assignedCallsCopy = swap(assignedCallsCopy, index2, index2+1);
                        currentCost = calculateCost.calculateCost(vehicle, assignedCallsCopy);
                    } else { //if last in list
                        checkedAll = true;
                    }
                } else { //if calls are not next to each other
                    if (index2 == assignedCallsCopy.size() -1){ //if call 2 is last in list (turn)
                        forward = false;
                        //swap index1 and index1+1
                        assignedCallsCopy = swap(assignedCallsCopy, index1, index1+1);                     
                    } else{ //move call 2 forward/backward
                        if (forward){
                            //swap index2 and index2+1
                            assignedCallsCopy = swap(assignedCallsCopy, index2, index2+1);
                        } else {
                            //swap index2 and index2-1
                            assignedCallsCopy = swap(assignedCallsCopy, index2, index2-1);                         
                        }
                    }
                    currentCost = calculateCost.calculateCost(vehicle, assignedCallsCopy);
                }
                if(!capacity.checkListCapacity(vehicle, assignedCallsCopy)){
                    continue;
                }
                else if(!checkTimeWindow.checkTimeWindow(vehicle, assignedCallsCopy)){
                    continue; 
                } else {
                    feasibleFound = true;
                    if(currentCost < currentbestCost){
                        currentbestCost = currentCost;
                        bestCurrentSpot = new ArrayList<>(assignedCallsCopy);
                        bestSolutions.add(new SolutionAndCost(vehicle, assignedCallsCopy, currentbestCost));
                    }
                }
            }

            if (!feasibleFound){
                //make cost to dummy cost if no feasible solution is found
                vehicle = 0;
                bestCurrentSpot = new ArrayList<>(solution.get(vehicle));
                bestCurrentSpot.add(0, call);
                bestCurrentSpot.add(0, call);
                currentbestCost = calculateCost.calculateCost(vehicle, bestCurrentSpot);
                bestSolutions.add(new SolutionAndCost(vehicle, bestCurrentSpot, currentbestCost));
                feasibleFound = true;
            }
            if (currentbestCost < bestCost && feasibleFound){
                bestCost = currentbestCost;
                bestSpot = new ArrayList<>(bestCurrentSpot);
                targetVehicle = vehicle;
            }
        }
        if (targetVehicle == -1){ //if absolutely no other than dummy possible
            targetVehicle = 0;
            bestSpot = new ArrayList<>(solution.get(targetVehicle));
            bestSpot.add(0, call);
            bestSpot.add(0, call);
            bestCost = calculateCost.calculateCost(targetVehicle, bestSpot);
            bestSolutions.add(new SolutionAndCost(targetVehicle, bestSpot, bestCost));
        }
        VehicleAndList vehicleAndList = new VehicleAndList(targetVehicle, bestSpot);
        return vehicleAndList;
    }

    private List<Integer> swap(List<Integer> list, int index1, int index2){
        int temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
        return list;
    }

}

