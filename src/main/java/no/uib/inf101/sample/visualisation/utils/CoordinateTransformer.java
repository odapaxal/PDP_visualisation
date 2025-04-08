package no.uib.inf101.sample.visualisation.utils;

import com.gurobi.gurobi.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.objects.TravelInfo;
import no.uib.inf101.sample.objects.TravelKey;
import no.uib.inf101.sample.parser.Read;

public class CoordinateTransformer {
    private final int width;
    private final int height;
    
    public CoordinateTransformer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public List<Node> createNodeList(){
        //random node allocation
        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i <= 39; i++) {
            int x = (int) (Math.random() * (width - 10) + 5);
            int y = (int) (Math.random() * (height - 10) + 5);
            nodes.add(new Node(i, x, y));
        }
        return nodes;
    }

}

    /* 
    public List<Node> createNodeList() {
        List<Node> nodes = new ArrayList<>();
        try {
            // Initialize Gurobi environment and model
            GRBEnv env = new GRBEnv(true);
            env.set("logFile", "gurobi.log");
            env.start();
            GRBModel model = new GRBModel(env);

            // Read input data
            Read read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
            HashMap<TravelKey, TravelInfo> travelInfoMap = read.getTravelMap();
            int numNodes = read.getNumNodes();
            int numVehicles = read.getNumVehicles();

            // Create decision variables for x and y coordinates
            GRBVar[] x = new GRBVar[numNodes];
            GRBVar[] y = new GRBVar[numNodes];
            for (int i = 0; i < numNodes; i++) {
                x[i] = model.addVar(5, width - 5, 0, GRB.CONTINUOUS, "x_" + i);
                y[i] = model.addVar(5, height - 5, 0, GRB.CONTINUOUS, "y_" + i);
            }

            // Define the objective function
            GRBQuadExpr objective = new GRBQuadExpr();
            double minDistance = 20;
            for (int i = 0; i < numNodes; i++) {
                for (int j = i + 1; j < numNodes; j++) {
                    double travelTime = getAverageDistance(travelInfoMap, numVehicles, i+1, j+1); // Adjusted for 1-based index

                    // Add (x[i] - x[j])^2 + (y[i] - y[j])^2 to the objective
                    objective.addTerm(1.0, x[i], x[i]);
                    objective.addTerm(1.0, x[j], x[j]);
                    objective.addTerm(-2.0, x[i], x[j]);
                    
                    objective.addTerm(1.0, y[i], y[i]);
                    objective.addTerm(1.0, y[j], y[j]);
                    objective.addTerm(-2.0, y[i], y[j]);

                    // Subtract travelTime^2 (constant term)
                    objective.addConstant(-2 * travelTime * travelTime);

                    GRBQuadExpr distanceConstraint = new GRBQuadExpr();
                    distanceConstraint.addTerm(1.0, x[i], x[i]);
                    distanceConstraint.addTerm(1.0, x[j], x[j]);
                    distanceConstraint.addTerm(-2.0, x[i], x[j]);
                    distanceConstraint.addTerm(1.0, y[i], y[i]);
                    distanceConstraint.addTerm(1.0, y[j], y[j]);
                    distanceConstraint.addTerm(-2.0, y[i], y[j]);
                    model.addQConstr(distanceConstraint, GRB.GREATER_EQUAL, minDistance*minDistance, "MinDist");
                }
            }
            model.setObjective(objective, GRB.MINIMIZE);

            // Optimize the model
            model.optimize();

            // Extract the solution
            for (int i = 0; i < numNodes; i++) {
                double xCoord = x[i].get(GRB.DoubleAttr.X);
                double yCoord = y[i].get(GRB.DoubleAttr.X);
                nodes.add(new Node(i+1, (int) xCoord, (int) yCoord));
            }

            // Dispose of the model and environment
            model.dispose();
            env.dispose();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    public double getAverageDistance(HashMap<TravelKey, TravelInfo> travelInfoMap, int numVehicles, int i, int j) {
        double totalDistance = 0;

        for (int k = 1; k <= numVehicles; k++) {
            TravelKey key = new TravelKey(k,i, j);
            System.out.println("Key: " + key+" for i: "+i+" j: "+j+" k: "+k);
            TravelInfo info = travelInfoMap.get(key);
            totalDistance += info.getTravelTime();
        }

        return totalDistance/numVehicles;
    }
}
*/