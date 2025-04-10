package no.uib.inf101.sample.visualisation.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.animation.AnimationManager;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;
import no.uib.inf101.sample.visualisation.utils.CoordinateTransformer;
import no.uib.inf101.sample.visualisation.utils.RouteManager;
import no.uib.inf101.sample.visualisation.utils.Solution;

public class MapPanel extends JPanel{
    int width = 1000;
    int height = 600;

    public LegendPanel legendPanel;
    private List<Node> nodes;
    private List<Route> routes;
    private List<AnimationManager> animations;
    private HashMap<Integer, List<Integer>> solution = new HashMap<>();
    Graphics2D g2;
    Solution solutionSolver;
    RouteManager routeManager;
    Read read;

    Random random = new Random();

    public MapPanel(Read read, LegendPanel legendPanel) {
        this.legendPanel = legendPanel;
        this.read = read;
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(new Color(135, 206, 235)); // Sky blue color

        CoordinateTransformer transformer = new CoordinateTransformer(width, height,read);
        nodes = transformer.createNodeList();
        routeManager = new RouteManager(read, nodes);
        solutionSolver = new Solution(read);

        if (solution.isEmpty()){
            routes = routeManager.createEmptyRoutes();
        } else {
            routeManager.formatRoutes(solution);
            routes = routeManager.getRoutes();
        }

        // Create a list of animations for each route
        animations = new ArrayList<>();
        for (Route route : routes) {
            AnimationManager animation = new AnimationManager(route, this::repaint);
            animations.add(animation);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        drawNodes(g2);
        System.out.println("repaint is called");
        // Draw all routes
        if (!solution.isEmpty()){
            System.out.println("Drawing routes...");
            for (int i = 0; i < routes.size(); i++) {
                System.out.println("Drawing route " + i);
                AnimationManager animation = animations.get(i);
                animation.drawCompletedSegments(g2);
                animation.drawAnimatedLine(g2);
            }
        }
    }
    
    private void drawNodes(Graphics2D g2) {
        
        for (Node node : nodes) {
            int x = node.x() - 5; // Adjust x to center the circle
            int y = node.y() - 5; // Adjust y to center the circle
            g2.setColor(Color.getColor("darkblue"));
            g2.fillOval(x, y, 10, 10); // Draw each node as a small circle
            g2.drawString(""+node.id(), x + 12, y + 10); // Label the node
        }
    }

    public void startAnimation() {
        System.out.println("Starting animation...");
        for (AnimationManager animation : animations) {
            animation.start();
        }
    }

    public void setSolution(HashMap<Integer, List<Integer>> solution) {
        this.solution = solution; // Update the solution

        // Reinitialize the routes based on the new solution
        routeManager.formatRoutes(solution);
        routes = routeManager.getRoutes();
        System.out.println("Routes updated: " + routes);

        for (int i = 0; i < routes.size(); i++) {
            AnimationManager animation = animations.get(i);
            animation.setRoute(routes.get(i));
            System.out.println("Animation updated: "+ animation.getRoute() + " and nodes " + animation.getRoute().nodes());
        }
        legendPanel.setMessage("Solution: "+solution.toString()+ " with routes "+ routes);
        // Trigger a repaint to reflect the changes
        repaint();
    }

    public HashMap<Integer, List<Integer>> getSolution() {
        return solution;
    }
    public Read getRead() {
        return read;
    }

}
