package no.uib.inf101.sample.visualisation.utils.NodeAllocation;

import java.util.ArrayList;
import java.util.List;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Tuple;


public class CoordinateTransformer {
    private final int width, height;
    private final List<Node> nodes, relevantNodes;
    private final List<List<Double>> distanceMatrix;
    private final CoordinateCalculation coordinateCalculation = new CoordinateCalculation();

    /**
     * Constructor for CoordinateTransformer
     * Sets Node coordinates based on time distance in data
     * @param width  the width of the frame
     * @param height the height of the frame
     * @param read   the Read object containing node data
     */
    public CoordinateTransformer(int width, int height, Read read) {
        this.width = width;
        this.height = height;

        NodeManager nodeManager = new NodeManager(read);
        this.nodes = nodeManager.getNodes();
        this.relevantNodes = nodeManager.getRelevantNodes();

        DistanceCalculator distanceCalculator = new DistanceCalculator(read, relevantNodes);
        this.distanceMatrix = distanceCalculator.getDistanceMatrix();

        transformNodes();
        new ScaleCoordinates(relevantNodes, width, height); // Scale coordinates to fit the frame
    }

    /** Calculates and sets new node coordinates */
    private void transformNodes() {
        List<Tuple<Integer, Integer>> xyMap = nodeMapper();
        if (xyMap.size() != relevantNodes.size()) {
            throw new IllegalArgumentException("xyMap size does not match relevantNodes size");
        }

        for (int i = 0; i < relevantNodes.size(); i++) {
            Node node = relevantNodes.get(i);
            Tuple<Integer, Integer> coordinates = xyMap.get(i);
            if (coordinates == null) {
                throw new IllegalArgumentException("Coordinates for node " + node.getId() + " are null");
            }
            node.setX(coordinates.getFirst() + width / 2);
            node.setY(coordinates.getSecond() + height / 2);
        }
    }

    /**
     * Maps nodes to coordinates using multilateration
     * @return a list of tuples representing the coordinates of each node
     */
    private List<Tuple<Integer, Integer>> nodeMapper() {
        List<Tuple<Integer, Integer>> xyMap = createInitialMapping();

        for (int u = 0; u < distanceMatrix.size(); u++) {
            if (xyMap.get(u) != null) continue;

            List<Tuple<Double, Double>> knownPoints = new ArrayList<>();
            List<Double> distances = new ArrayList<>();

            for (int v = 0; v < distanceMatrix.get(u).size(); v++) {
                if (xyMap.get(v) != null) {
                    Tuple<Integer, Integer> point = xyMap.get(v);
                    knownPoints.add(new Tuple<>((double) point.getFirst(), (double) point.getSecond()));
                    distances.add(distanceMatrix.get(u).get(v));
                }
            }

            if (knownPoints.size() < 2) continue;

            // Perform multilateration to find the exact coordinates of node u
            try {
                Tuple<Double, Double> result = coordinateCalculation.multilateration(knownPoints, distances);
                xyMap.set(u, new Tuple<>((int) Math.round(result.getFirst()), (int) Math.round(result.getSecond())));
            } catch (Exception e) {
                System.err.println("Multilateration failed for node " + u + ": " + e.getMessage());
            }
        }
        return xyMap;
    }

    /**
     * Creates the first three node coordinates based on the distance matrix
     * @return a list of tuples representing the coordinates of the first three nodes
     * @throws IllegalArgumentException if the distance from node 1 to any other node is null
     */
    private List<Tuple<Integer, Integer>> createInitialMapping() {
        List<Tuple<Integer, Integer>> xyMap = new ArrayList<>();
        for (int i = 0; i < distanceMatrix.size(); i++) xyMap.add(null);

        xyMap.set(0, new Tuple<>(0, 0)); // First node at (0, 0)

        double initialXyOffset = 0;
        int nextNode = -1;

        // Finds the node with the largest distance from node 1 and sets as the next node
        for (int i = 1; i < distanceMatrix.size(); i++) {
            Double distance = distanceMatrix.get(0).get(i);
            if (distance == null) throw new IllegalArgumentException("Distance from node 1 to node " + i + " is null");

            double temp = distance / Math.sqrt(2);
            if (temp > initialXyOffset) {
                initialXyOffset = temp;
                nextNode = i;
            }
        }

        xyMap.set(nextNode, new Tuple<>((int) initialXyOffset, (int) initialXyOffset));

        // Calculate the coordinates of the third node using circle-circle intersection
        Tuple<Integer, Integer> v1 = coordinateCalculation.circleCircleIntersection(
            xyMap.get(0), 
            xyMap.get(nextNode), 
            distanceMatrix.get(0).get(1), 
            distanceMatrix.get(0).get(nextNode)
        );

        if (v1 == null) throw new IllegalArgumentException("No intersection found between circles");

        xyMap.set(2, v1); // Set coordinates for the third node
        return xyMap;
    }

    public List<Node> getRelevantNodes() {
        return relevantNodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}