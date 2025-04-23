package no.uib.inf101.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;
import no.uib.inf101.sample.visualisation.utils.RouteManager;
import no.uib.inf101.sample.visualisation.utils.NodeAllocation.CoordinateTransformer;

public class RouteManagerTest {
    RouteManager routeManager;
    Read read;


    public RouteManagerTest() {
        try {
            read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
            CoordinateTransformer transformer = new CoordinateTransformer(1000, 600, read);
            List<Node> nodes = transformer.getNodes();
            routeManager = new RouteManager(read, nodes);
        } catch (IOException e) {
            e.printStackTrace();
            routeManager = null; // Handle initialization failure
        }
    }
    @Test
    void testCreateEmptyRoutes() {
        List<Route> routes = routeManager.createEmptyRoutes();        
        assertEquals(0, routes.get(0).nodes().size(), "Route list is not empty");
    }

    @Test
    void testFormatRoutes() {
        // Create a sample solution
        HashMap<Integer, List<Integer>> solution = new HashMap<>();
        solution.put(3, List.of(1, 1, 3,3));
        solution.put(1, List.of(4, 4));

        // Format the routes
        List<Route> formattedRoutes = routeManager.formatRoutes(solution);

        // Check if the formatted routes match the expected output
        assertEquals(2, formattedRoutes.size(), "Formatted routes size does not match expected size");

        // Check if routes have the expected number of nodes
        assertEquals(3, formattedRoutes.get(0).nodes().size(), "Route 1 does not have the expected number of nodes");
        assertEquals(5, formattedRoutes.get(1).nodes().size(), "Route 2 does not have the expected number of nodes");

        // Check if home nodes are correct
        assertEquals(read.getVehicles().get(0).getHomeNode(), formattedRoutes.get(0).nodes().get(0).getId(), "Home node ID does not match expected value");
        assertEquals(read.getVehicles().get(2).getHomeNode(), formattedRoutes.get(1).nodes().get(0).getId(), "Home node ID does not match expected value");

        // Check if route contains the expected nodes for call 4 in vehicle 1
        int pickup = read.getCalls().get(3).getOriginNode();
        int delivery = read.getCalls().get(3).getDestinationNode();
        List<Node> routeNodes = formattedRoutes.get(0).nodes();

        // skipping the first home node of the vehicle in the route
        assertEquals(pickup, routeNodes.get(1).getId(), "Pickup node ID does not match expected value");
        assertEquals(delivery, routeNodes.get(2).getId(), "Delivery node ID does not match expected value");
    }
}
