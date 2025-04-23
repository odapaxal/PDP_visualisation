package no.uib.inf101.sample.visualisation.utils.NodeAllocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;

public class DistanceCalculator {
    Read read;
    private List<List<Double>> distanceMatrix;
    private List<Node> relevantNodes;

    /**
     * Constructor for the DistanceCalculator
     * Sets the distance matrix based on the average travel time from the data
     * @param read
     * @param relevantNodes
     */
    public DistanceCalculator(Read read, List<Node> relevantNodes) {
        this.relevantNodes = relevantNodes;
        this.read = read;
        setDistanceMatrix();
    }

    private void setDistanceMatrix() {
        distanceMatrix = new ArrayList<>();
        HashMap<TravelKey, TravelInfo> travelInfoMap = read.getTravelMap();
    
        for (Node node : relevantNodes) {
            List<Double> row = new ArrayList<>();
            for (Node node2 : relevantNodes){
                if (node.getId() == node2.getId()){
                    row.add(0.0);
                } else {
                    double distance = getAverageDistance(travelInfoMap, read.getNumVehicles(), node.getId(), node2.getId());
                    row.add(distance);
                }
            }
            distanceMatrix.add(row);
        }
    }  

    /**
     * Helper function to find average time 
     * @param travelInfoMap
     * @param numVehicles
     * @param node i
     * @param node j
     * @return average time distance
     */
    private double getAverageDistance(HashMap<TravelKey, TravelInfo> travelInfoMap, int numVehicles, int i, int j) {
        double totalDistance = 0;

        for (int k = 1; k <= numVehicles; k++) {
            TravelKey key = new TravelKey(k,i, j);
            TravelInfo info = travelInfoMap.get(key);
            totalDistance += info.getTravelTime();
        }

        return totalDistance/numVehicles;
    }

    public List<List<Double>> getDistanceMatrix() {
        return distanceMatrix;
    }
}
