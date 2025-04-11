package no.uib.inf101.sample.visualisation.utils;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class RouteManager {
    private List<Route> routes;
    private List<Node> allNodes;
    Read read;
    List<Color> colours;

    public RouteManager(Read read, List<Node> allNodes) {
        this.read = read;
        this.allNodes = allNodes;
        this.colours = randomColours();
    }
    
    public List<Route> formatRoutes(HashMap<Integer, List<Integer>> solution){
        routes = new ArrayList<>();

        for (var entry : solution.entrySet()) {
            int vehicle = entry.getKey();
            System.out.println("Vehicle: " + vehicle);
            System.out.println("Assigned calls: " + entry.getValue());

            List<Integer> assignedCalls = entry.getValue();
            assignedCalls = new ArrayList<>(fromCallToNode(assignedCalls, vehicle));
            System.out.println("Assigned calls with nodes: " + assignedCalls);

            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < assignedCalls.size(); i++) {
                nodes.add(allNodes.get(assignedCalls.get(i))); 
            }
            Route route = new Route(colours.get(vehicle), nodes, vehicle); 
            routes.add(route);
        }
        System.out.println("Routes: " + routes);
        return routes;
    }

    private List<Color> randomColours() {
        List<Color> colours = new ArrayList<>();
        colours.add(new Color(128, 0, 128)); // Purple
        colours.add(new Color(255, 0, 0)); // Red
        colours.add(new Color(255, 255, 0)); // Yellow
        colours.add(new Color(255, 165, 0)); // Orange
        
        return colours;
    }

    private List<Integer> fromCallToNode(List<Integer> assignedCalls, int vehicle) {
        List<Integer> calls = new ArrayList<>(assignedCalls);
        List<Integer> nodes = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();

        // add home node if not dummy
        if (vehicle != 0){
            nodes.add(read.getVehicles().get(vehicle - 1).getHomeNode());
        }
        
        for (int i = 0; i < calls.size(); i++) {
            int call = calls.get(i);
            int node;
            if (visited.contains(call)){
                node = read.getCalls().get(call-1).getDestinationNode();
            } else {
                node = read.getCalls().get(call-1).getOriginNode();
                visited.add(call);
            }
            nodes.add(node);
        }
        return nodes;
    }

    public List<Route> createEmptyRoutes(){
        routes = new ArrayList<>();
        
        for (int i = 0; i <= read.getNumVehicles(); i++){
            List<Node> nodes = new ArrayList<>();
            Route route = new Route(colours.get(i), nodes, i); 
            routes.add(route);
        }
        return routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

}
