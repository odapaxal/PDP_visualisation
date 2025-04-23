package no.uib.inf101.sample.visualisation.utils.NodeAllocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import no.uib.inf101.sample.visualisation.objects.Tuple;

/**
 * This class contains methods for calculating coordinates of nodes using multilateration and circle-circle intersection.
 * It uses Apache Commons Math library for matrix operations.
 */
public class CoordinateCalculation {

     /**
     * Multilateration algorithm from python script made by INF273 group leader Eirik Petrie
     * @param knownPoints
     * @param distances
     * @return Approximate coordinates of the node
     */
    public Tuple<Double, Double> multilateration(List<Tuple<Double, Double>> knownPoints, List<Double> distances) {
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
     * @return Coordinates of the intersection point
     */
    public Tuple<Integer, Integer> circleCircleIntersection(Tuple<Integer, Integer> p1, Tuple<Integer, Integer> p2, double r1, double r2) {
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
}
