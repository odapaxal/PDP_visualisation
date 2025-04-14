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
    
    /**
     * Turn solution into a Route object list with correct node numbers
     * @param solution
     * @return list<Route> with <Color color, List<Node>, int vehicle>
     */
    public List<Route> formatRoutes(HashMap<Integer, List<Integer>> solution){
        routes = new ArrayList<>();

        for (var entry : solution.entrySet()) {
            int vehicle = entry.getKey();
            List<Integer> assignedCalls = entry.getValue();
       
            // change from call number to pickup and delivery node
            List<Node> nodes = new ArrayList<>(fromCallToNode(assignedCalls, vehicle));

            Route route = new Route(colours.get(vehicle), nodes, vehicle); 
            routes.add(route);
        }
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

    /**
     * Assigns nodes to a list and sets pickup boolean
     * @param assignedCalls
     * @param vehicle
     * @return
     */
    private List<Node> fromCallToNode(List<Integer> assignedCalls, int vehicle) {
        List<Integer> calls = new ArrayList<>(assignedCalls);
        List<Node> nodes = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();

        // add home node if not dummy
        if (vehicle != 0){
            int homeNode = read.getVehicles().get(vehicle - 1).getHomeNode();
            nodes.add(allNodes.get(homeNode));
        }
        
        // format to origin and destination nodes
        for (int i = 0; i < calls.size(); i++) {
            int call = calls.get(i);
            Node node = null;
            if (visited.contains(call)){
                node = allNodes.get(read.getCalls().get(call-1).getDestinationNode());
            } else {
                node = allNodes.get(read.getCalls().get(call-1).getOriginNode());
                node.setPickup(true);
                visited.add(call);
            }
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * Create empty List<Node>'s for each vehicle 
     * @return
     */
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
