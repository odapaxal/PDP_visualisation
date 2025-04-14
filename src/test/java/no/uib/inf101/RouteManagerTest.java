package no.uib.inf101;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;
import no.uib.inf101.sample.visualisation.utils.CoordinateTransformer;
import no.uib.inf101.sample.visualisation.utils.RouteManager;

public class RouteManagerTest {
    RouteManager routeManager;


    public RouteManagerTest() {
        try {
            Read read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
            CoordinateTransformer transformer = new CoordinateTransformer(1000, 600, read);
            List<Node> nodes = transformer.createNodeList();
            routeManager = new RouteManager(read, nodes);
        } catch (IOException e) {
            e.printStackTrace();
            routeManager = null; // Handle initialization failure
        }
    }
    @Test
    void testCreateEmptyRoutes() {
        List<Route> routes = routeManager.createEmptyRoutes();        
        if (!routes.get(0).nodes().isEmpty()){
            throw new Error("Route list is not empty");
        }
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
        if (formattedRoutes.size() != 2) {
            throw new Error("Formatted routes size does not match expected size");
        }
    }
}
