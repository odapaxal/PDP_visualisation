package no.uib.inf101.sample.visualisation.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.math3.linear.*;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.objects.Vehicle;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Tuple;

public class CoordinateTransformer {
    private final int width;
    private final int height;
    Read read;
    private List<Node> nodes;
    private List<Node> relevantNodes;
    private List<List<Double>> distanceMatrix;
    
    /**
     * Creates a list of all nodes, and sets coordinates for the relevant nodes for the data
     * @param frame width
     * @param frame height
     * @param read
     */
    public CoordinateTransformer(int width, int height, Read read) {
        this.width = width;
        this.height = height;
        this.read = read;
        createNodeList();
        addRelevantNodes();
        transformNodes();
        scaleNodeCoordinates();
    }

    /**
     * Initialises a node list with (0,0) coordinates for all
     */
    private void createNodeList(){
        //random node allocation
        nodes = new ArrayList<>();
        for (int i = 1; i <= read.getNumNodes(); i++) {
            nodes.add(new Node(i, 0, 0, false));
        }
    }

    /**
     * Creates a new list containing only the nodes that are relevant for the data set
     */
    private void addRelevantNodes() {
        relevantNodes = new ArrayList<>();

        // add vehicle home nodes
        List<Vehicle> vehicles = read.getVehicles();
        for (int i = 0; i < vehicles.size(); i++) {
            int homeNode = vehicles.get(i).getHomeNode();
            relevantNodes.add(nodes.get(homeNode-1));
        }
        // add call origin and destination nodes
        List<Call> calls = read.getCalls();
        for (int i = 0; i < read.getNumCalls(); i++) {
            int originNode = calls.get(i).getOriginNode();
            int destinationNode = calls.get(i).getDestinationNode();
            relevantNodes.add(nodes.get(originNode-1));
            relevantNodes.add(nodes.get(destinationNode-1));
        }
    }

     /**
     * Sets the distance matrix based on the average travel time from the data
     */
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

    /**
     * Calculates and sets the new coordinates for each node
     */
    private void transformNodes() {
        List<Tuple<Integer, Integer>> xyMap = nodeMapper();

        if (xyMap.size() != relevantNodes.size()) {
            throw new IllegalArgumentException("xyMap size does not match relevantNodes size");
        }

        for (int i = 0; i < relevantNodes.size(); i++) {
            Node node = relevantNodes.get(i);
            Tuple<Integer, Integer> coordinates = xyMap.get(i);
            if (coordinates != null) {
                // Create position with the transformed coordinates
                int adjustedX = coordinates.getFirst() + width/2;
                int adjustedY = coordinates.getSecond() + height/2;
                node.setX(adjustedX);
                node.setY(adjustedY);
            } else {
                throw new IllegalArgumentException("Coordinates for node " + node.getId() + " are null");
            }
        }
    }

    /**
     * Scales the nodes to be spread out inside the frame
     * Written by ChatGPT
     */
    private void scaleNodeCoordinates() {
        // Step 1: Find the bounding box of the current coordinates
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
    
        for (Node node : relevantNodes) {
            double x = node.getX();
            double y = node.getY();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
    
        // Step 2: Calculate the width and height of the bounding box
        double boundingBoxWidth = maxX - minX;
        double boundingBoxHeight = maxY - minY;
    
        // Step 3: Calculate the scaling factor to fit the screen
        double scaleX = (width - 20) / boundingBoxWidth; // Leave 5px padding on each side
        double scaleY = (height - 20) / boundingBoxHeight;
        double scalingFactor = Math.min(scaleX, scaleY); // Maintain aspect ratio
    
        // Step 4: Center the coordinates
        double offsetX = (width - (boundingBoxWidth * scalingFactor)) / 2 - (minX * scalingFactor);
        double offsetY = (height - (boundingBoxHeight * scalingFactor)) / 2 - (minY * scalingFactor);
    
        // Step 5: Apply scaling and centering to each node
        for (Node node : relevantNodes) {
            int scaledX = (int) Math.round(node.getX() * scalingFactor + offsetX);
            int scaledY = (int) Math.round(node.getY() * scalingFactor + offsetY);

            if (scaledX < 0 || scaledY < 0) {
                scaledX = 5;
                scaledY = 5;
            }
            if (scaledX > width || scaledY > height) {
                scaledX = width -5;
                scaledY = height -5;
            }

            node.setX(scaledX);
            node.setY(scaledY);
            System.out.println("New coordinates: "+node.getX()+", "+node.getY());
        }

    }

    /**
     * Calculates all the remaning node coordinates based on the first three node placements
     * @return XYmap 
     */
    private List<Tuple<Integer, Integer>> nodeMapper() {
        setDistanceMatrix();
        List<Tuple<Integer, Integer>> xyMap = createInitialMapping();

        for (int u = 0; u < distanceMatrix.size(); u++) {
            // Skip if the node already has coordinates
            if (xyMap.get(u) != null) {
                continue;
            }

            // Collect known points and distances
            List<Tuple<Double, Double>> knownPoints = new ArrayList<>();
            List<Double> distances = new ArrayList<>();

            for (int v = 0; v < distanceMatrix.get(u).size(); v++) {
                if (xyMap.get(v) != null) {
                    // Add the known point and its distance
                    Tuple<Integer, Integer> point = xyMap.get(v);
                    knownPoints.add(new Tuple<>((double) point.getFirst(), (double) point.getSecond()));
                    distances.add(distanceMatrix.get(u).get(v));
                }
            }

            // Skip if fewer than 2 known points are available
            if (knownPoints.size() < 2) {
                continue;
            }

            // Step 4: Use multilateration to calculate the coordinates
            try {
                Tuple<Double, Double> result = multilateration(knownPoints, distances);
                xyMap.set(u, new Tuple<>((int) Math.round(result.getFirst()), (int) Math.round(result.getSecond())));
            } catch (Exception e) {
                // Handle cases where multilateration fails (e.g., singular matrix)
                System.err.println("Multilateration failed for node " + u + ": " + e.getMessage());
            }
        }
        return xyMap;
    }

    /**
     * Creates an initial mapping of three points with the correct euclidian distance
     * @return initial XY map
     */
    public List<Tuple<Integer, Integer>> createInitialMapping() {
        // Initialize the xyMap list with null values
        List<Tuple<Integer, Integer>> xyMap = new ArrayList<>();
        for (int i = 0; i < distanceMatrix.size(); i++) {
            xyMap.add(null); 
        }

        xyMap.set(0, new Tuple<>(0, 0)); // Set the first node's coordinates to (0, 0)

        double initialXyOffset = 0;
        int nextNode = -1;

        // Find the next node with the largest offset
        for (int i = 1; i < distanceMatrix.size(); i++) {
            if (distanceMatrix.get(0).get(i) == null) {
                throw new IllegalArgumentException("Distance from node 1 to node " + i + " is null");
            }
            double temp = distanceMatrix.get(0).get(i) / Math.sqrt(2);
            if (temp > initialXyOffset) {
                initialXyOffset = temp;
                nextNode = i;
            }
        }
   
        // Set the coordinates of the next node
        xyMap.set(nextNode, new Tuple<>((int) initialXyOffset, (int) initialXyOffset));

        // Calculate the intersection point for node 3
        Tuple<Integer, Integer> v1 = circleCircleIntersection(
            xyMap.get(0), 
            xyMap.get(nextNode), 
            distanceMatrix.get(0).get(1), 
            distanceMatrix.get(0).get(nextNode)
        );

        if (v1 == null) {
            throw new IllegalArgumentException("No intersection found between circles");
        }

        xyMap.set(2, v1); // Set the coordinates of node 3

        return xyMap;
    }

    /**
     * Chat GPT
     * @param knownPoints
     * @param distances
     * @return
     */
    public static Tuple<Double, Double> multilateration(List<Tuple<Double, Double>> knownPoints, List<Double> distances) {
        // Initialize matrices A and b
        List<double[]> A = new ArrayList<>();
        List<Double> b = new ArrayList<>();

        // Reference point (x0, y0) and its distance r0
        double x0 = knownPoints.get(0).getFirst();
        double y0 = knownPoints.get(0).getSecond();
        double r0 = distances.get(0);

        // Build the A matrix and b vector
        for (int i = 1; i < knownPoints.size(); i++) {
            double xi = knownPoints.get(i).getFirst();
            double yi = knownPoints.get(i).getSecond();
            double ri = distances.get(i);

            // Add row to A matrix
            A.add(new double[]{2 * (xi - x0), 2 * (yi - y0)});

            // Add value to b vector
            b.add(Math.pow(r0, 2) - Math.pow(ri, 2) + Math.pow(xi, 2) - Math.pow(x0, 2) + Math.pow(yi, 2) - Math.pow(y0, 2));
        }

        // Convert A and b to arrays for matrix operations
        RealMatrix matrixA = new Array2DRowRealMatrix(A.toArray(new double[0][]));
        RealVector vectorB = new ArrayRealVector(b.stream().mapToDouble(Double::doubleValue).toArray());

        // Solve the least squares problem using Apache Commons Math
        DecompositionSolver solver = new SingularValueDecomposition(matrixA).getSolver();
        RealVector solution = solver.solve(vectorB);

        return new Tuple<>(solution.getEntry(0), solution.getEntry(1));
    }
    
   
    /**
     * Circle circle intersection algorithm based on python script provided by group leader in INF273 Eirik Petrie
     * @param p1
     * @param p2
     * @param r1
     * @param r2
     * @return
     */
    private Tuple<Integer, Integer> circleCircleIntersection(Tuple<Integer, Integer> p1, Tuple<Integer, Integer> p2, double r1, double r2) {
        int x1 = p1.getFirst();
        int y1 = p1.getSecond();
        int x2 = p2.getFirst();
        int y2 = p2.getSecond();

        int dx = x2 - x1;
        int dy = y2 - y1;

        // Calculate the Euclidean distance between the two circle centers
        double distance = Math.hypot(dx, dy);

        if (distance > r1 + r2 || distance < Math.abs(r1 - r2) || distance == 0) {
            return null; // No intersection
        }

        // Calculate the distance from the first circle's center to the line connecting the intersection points
        double a = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(distance, 2)) / (2 * distance);

        // Calculate the coordinates of the midpoint on the line connecting the intersection points
        double xm = x1 + a * dx / distance;
        double ym = y1 + a * dy / distance;

        // Calculate the perpendicular distance from the midpoint to the intersection points
        double h = Math.sqrt(Math.pow(r1, 2) - Math.pow(a, 2));

        // Calculate the offsets for the intersection points
        double rx = -dy * h / distance;
        double ry = dx * h / distance;

        // coordinates of one of the intersection points
        int newX1 = (int) (xm + rx);
        int newY1 = (int) (ym + ry);

        Tuple<Integer, Integer> intersection1 = new Tuple<>(newX1, newY1);

        return intersection1;
    }

    public List<Node> getRelevantNodes() {
        return relevantNodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

}