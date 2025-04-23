package no.uib.inf101.sample.visualisation.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.animation.AnimationManager;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;
import no.uib.inf101.sample.visualisation.utils.RouteManager;
import no.uib.inf101.sample.visualisation.utils.Solution;
import no.uib.inf101.sample.visualisation.utils.NodeAllocation.CoordinateTransformer;

public class MapPanel extends JPanel{
    int width = 1000;
    int height = 600;

    public LegendPanel legendPanel;
    private List<Node> nodes;
    private List<Node> relevantNodes;
    private List<Route> routes;
    private List<AnimationManager> animations;
    private HashMap<Integer, List<Integer>> solution = new HashMap<>();
    private List<Image> vehicleImages;
    private List<Image> cargoImages;
    Graphics2D g2;
    Solution solutionSolver;
    RouteManager routeManager;
    Read read;

    Random random = new Random();

    /**
     * Creates a MapPanel to visualise the routes and nodes.
     * @param read data to be visualised
     * @param legendPanel to interact with
     */
    public MapPanel(Read read, LegendPanel legendPanel) {
        this.legendPanel = legendPanel;
        this.read = read;
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(new Color(135, 206, 235)); // Sky blue color

        // Initialise routes and nodes
        CoordinateTransformer transformer = new CoordinateTransformer(width, height,read);
        nodes = transformer.getNodes();
        relevantNodes = transformer.getRelevantNodes();
        routeManager = new RouteManager(read, nodes);
        routes = routeManager.createEmptyRoutes();

        solutionSolver = new Solution(read);
        createImages();

        // Create a list of animations for each route
        animations = new ArrayList<>();
        for (Route route : routes) {
            AnimationManager animation = new AnimationManager(route, this::repaint, this);
            animations.add(animation);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        drawNodes(g2);
        // Draw all routes if ALNS has not been run
        if (solution.isEmpty()){
            drawObjects(g2);
        }
   
        // Draw all routes if ALNS has been run
        if (!solution.isEmpty()){
            for (int i = 0; i < routes.size(); i++) {
                AnimationManager animation = animations.get(i);
                g2.setColor(animation.getRoute().color());
                animation.draw(g2);
            }
        }
    }
    
    private void drawNodes(Graphics2D g2) {
        // Draw all relevant nodes in the map
        for (Node node : relevantNodes) {
            int x = node.getX() - 5; // Adjust x to center the circle
            int y = node.getY() - 5; // Adjust y to center the circle
            g2.setColor(Color.getColor("darkblue"));
            g2.fillOval(x, y, 10, 10); // Draw each node as a small circle
            g2.drawString(""+node.getId(), x + 12, y + 10); // Label the node

        }
    }

    private void drawObjects(Graphics2D g2) {
        // Draw vehicles
        for (int i = 0; i < read.getNumVehicles(); i++) {
            Node homenode = nodes.get(read.getVehicles().get(i).getHomeNode()-1);
            if (homenode == null) {
                throw new Error("Home node is null for vehicle: " + i);
            }
            int x = homenode.getX() - 30; // Adjust x to center the image
            int y = homenode.getY() - 20; // Adjust y to center the image
            g2.drawImage(vehicleImages.get(i+1), x, y, 60, 40, null);
        }

        // Draw cargo
        for (int i = 0; i < read.getNumCalls(); i++) {
            Node cargoOrigin = nodes.get(read.getCalls().get(i).getOriginNode()-1);
            int x = cargoOrigin.getX() - 30; // Adjust x to center the image
            int y = cargoOrigin.getY() - 20; // Adjust y to center the image
            g2.drawImage(cargoImages.get(0), x, y, 60, 40, null);
        }
    }

    private void createImages() {
        vehicleImages = new ArrayList<>();
        cargoImages = new ArrayList<>();
        try {
            vehicleImages.add(ImageIO.read(new File("src/main/resources/PurpleBoat.png")));
            vehicleImages.add(ImageIO.read(new File("src/main/resources/RedBoat.png")));
            vehicleImages.add(ImageIO.read(new File("src/main/resources/YellowBoat.png")));
            vehicleImages.add(ImageIO.read(new File("src/main/resources/OrangeBoat.png")));

            cargoImages.add(ImageIO.read(new File("src/main/resources/Cargo.png")));
        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Starts animation for every animationManager */
    public void startAnimation() {
        System.out.println("Starting animation...");
        for (AnimationManager animation : animations) {
            animation.start();
        }
    }

    /**
     * Sets the solution and updates the routes accordingly.
     * @param solution
     */
    public void setSolution(HashMap<Integer, List<Integer>> solution) {
        this.solution = solution; // Update the solution
 
        // Reinitialize the routes based on the new solution
        routeManager.formatRoutes(solution);
        routes = routeManager.getRoutes();

        for (int i = 0; i < routes.size(); i++) {
            AnimationManager animation = animations.get(i);
            animation.setRoute(routes.get(i));
        }
        legendPanel.setMessage("Solution: "+solution.toString()+ " with routes "+ routes); // not initialised

        repaint();
    }

    public HashMap<Integer, List<Integer>> getSolution() {
        return solution;
    }
    public Read getRead() {
        return read;
    }
    public List<Route> getRoutes() {
        return routes;
    }
    public List<Image> getVehicleImages() {
        return vehicleImages;
    }
    public List<Image> getCargoImages() {
        return cargoImages;
    }

}
