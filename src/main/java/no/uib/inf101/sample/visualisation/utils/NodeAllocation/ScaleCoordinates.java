package no.uib.inf101.sample.visualisation.utils.NodeAllocation;

import java.util.List;
import no.uib.inf101.sample.visualisation.objects.Node;

public class ScaleCoordinates {
    private List<Node> relevantNodes;
    private int width;
    private int height;

    /**
     * Constructor for the ScaleCoordinates class
     * @param relevantNodes List of nodes to be scaled
     * @param width Width of the frame
     * @param height Height of the frame
     */
    public ScaleCoordinates(List<Node> relevantNodes, int width, int height) {
        this.relevantNodes = relevantNodes;
        this.width = width;
        this.height = height;
        scaleNodeCoordinates();
    }

    /**
     * Scales the nodes to be spread out inside the frame
     */
    private void scaleNodeCoordinates() {
        // Find bounding box
        double minX = relevantNodes.stream().mapToDouble(Node::getX).min().orElse(0);
        double maxX = relevantNodes.stream().mapToDouble(Node::getX).max().orElse(width);
        double minY = relevantNodes.stream().mapToDouble(Node::getY).min().orElse(0);
        double maxY = relevantNodes.stream().mapToDouble(Node::getY).max().orElse(height);

        // Calculate scaling factor and offsets
        double padding = 20;
        double scaleX = (width - padding) / (maxX - minX);
        double scaleY = (height - padding) / (maxY - minY);
        double scalingFactor = Math.min(scaleX, scaleY);
        double offsetX = (width - (maxX - minX) * scalingFactor) / 2 - minX * scalingFactor;
        double offsetY = (height - (maxY - minY) * scalingFactor) / 2 - minY * scalingFactor;

        // Scale and center nodes
        relevantNodes.forEach(node -> {
            int scaledX = (int) Math.round(node.getX() * scalingFactor + offsetX);
            int scaledY = (int) Math.round(node.getY() * scalingFactor + offsetY);
            node.setX(Math.max(5, Math.min(width - 5, scaledX)));
            node.setY(Math.max(5, Math.min(height - 5, scaledY)));
        });
    }
}
