package no.uib.inf101.sample.visualisation.objects;

import java.awt.Color;
import java.util.List;

/**
 * Represents a route for a vehicle in the visualisation.
 * Each route consists of a color, list of nodes and a vehicle number.
 */
public record Route(Color color, List<Node> nodes, int vehicle) {
    
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Route for vehicle ").append(vehicle).append(": ");
        for (Node node : nodes) {
            sb.append(node.getId()).append(" ");
        }
        return sb.toString();
    }
}
