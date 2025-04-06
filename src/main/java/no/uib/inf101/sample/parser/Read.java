package no.uib.inf101.sample.parser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.NodeInfo;
import no.uib.inf101.sample.objects.NodeKey;
import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.objects.Vehicle;



public class Read {
    private int numNodes;
    private int numVehicles;
    private List<Vehicle> vehicles = new ArrayList<>();
    private int numCalls;
    private Map<Integer, List<Integer>> vehicleCalls = new HashMap<>();
    private List<Call> calls = new ArrayList<>();
    private List<TravelInfo> travelInfos = new ArrayList<>();
    private HashMap<TravelKey, TravelInfo> travelInfoMap = new HashMap<>();
    private HashMap<NodeKey, NodeInfo> nodeInfoMap = new HashMap<>();
    private List<NodeInfo> nodeInfos = new ArrayList<>();

    public Read(String filepath) throws IOException{
        parseFile(filepath);
    }
    
    /**
     * Parses the input file and populates the data structures
     * @param filePath The path to the input file
     * @throws IOException 
     */
    public void parseFile(String filePath) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("%")) continue;
                // if variables are empty, read them until the next %

                if (numNodes == 0) {
                    numNodes = Integer.parseInt(line);
                } else if (numVehicles == 0){
                    numVehicles = Integer.parseInt(line);
                } else if (vehicles.size() < numVehicles){
                    parseVehicleData(line);
                } else if (numCalls == 0){
                    numCalls = Integer.parseInt(line);
                } else if (vehicleCalls.size() < numVehicles){
                    parseVehicleCallList(line);
                } else if (calls.size() < numCalls) {
                    parseCallData(line);
                } else if (travelInfos.size() < (numNodes*numNodes*numVehicles)) {
                    parseTravelData(line);
                } else if (nodeInfos.size() < (numVehicles*numCalls)){
                    parseNodeInfo(line);
                }             
            
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void parseNodeInfo(String line) {
        String [] parts = line.split(",");
        NodeInfo newN = new NodeInfo(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]), 
            Integer.parseInt(parts[2]), 
            Integer.parseInt(parts[3]), 
            Integer.parseInt(parts[4]), 
            Integer.parseInt(parts[5])
        );
        nodeInfos.add(newN);
        NodeKey key = new NodeKey(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        nodeInfoMap.put(key, newN);
    }

    private void parseTravelData(String line) {
        String[] parts = line.split(",");
        TravelInfo newT = new TravelInfo(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            Integer.parseInt(parts[4])
        );
        travelInfos.add(newT);
        TravelKey key = new TravelKey(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        travelInfoMap.put(key, newT);
    }

    private void parseCallData(String line) {
        String[] items = line.split(",");
        calls.add(new Call(
            Integer.parseInt(items[0]),
            Integer.parseInt(items[1]),
            Integer.parseInt(items[2]),
            Integer.parseInt(items[3]),
            Integer.parseInt(items[4]),
            Integer.parseInt(items[5]),
            Integer.parseInt(items[6]),
            Integer.parseInt(items[7]),
            Integer.parseInt(items[8])
        ));
    }

    private void parseVehicleCallList(String line) {
        String[] items = line.split(",");
        int vehicleIndex = Integer.parseInt(items[0]);

        List<Integer> callsList = new ArrayList<>();
        for (int i = 1; i < items.length; i++) {
            callsList.add(Integer.parseInt(items[i]));
        }
        vehicleCalls.put(vehicleIndex, callsList);
    }

    private void parseVehicleData(String line) {
        String[] items = line.split(",");
        vehicles.add(new Vehicle(
            Integer.parseInt(items[0]),
            Integer.parseInt(items[1]),
            Integer.parseInt(items[2]),
            Integer.parseInt(items[3])));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of Nodes: ").append(numNodes).append("\n");
        sb.append("Number of Vehicles: ").append(numVehicles).append("\n");
        sb.append("Number of Calls: ").append(numCalls).append("\n");
        sb.append("Vehicles: ").append(vehicles).append("\n");
        sb.append("Calls: ").append(calls).append("\n");
        return sb.toString();
    }


    public int getNumNodes() { return numNodes; }
    public int getNumVehicles() { return numVehicles; }
    public int getNumCalls() { return numCalls; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public List<Call> getCalls() { return calls; }
    public Map<Integer, List<Integer>> getVehicleCalls() { return vehicleCalls; }
    public List<TravelInfo> getTravelInfos() { return travelInfos; }
    public List<NodeInfo> getNodeInfos() {return nodeInfos;}
    public HashMap<TravelKey, TravelInfo> getTravelMap() {return travelInfoMap;}
    public HashMap<NodeKey, NodeInfo> getNodeMap() {return nodeInfoMap;}
}