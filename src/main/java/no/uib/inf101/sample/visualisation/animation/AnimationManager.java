package no.uib.inf101.sample.visualisation.animation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import javax.swing.Timer;

import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class AnimationManager {
    private Timer timer; 
    private Route route;
    private int animationStep = 0;
    private int cargo = 0;
    private int currentNode = 0;
    private int startIndex;
    private int endIndex;
    List<Integer> assignedCalls;
    private final Runnable repaint;
    private MapPanel mapPanel;
    private Node start;
    private Node end;

    
    /**
     * Constructor for AnimationManager
     * @param route
     * @param repaint
     * Starts a new timer for animating the route
     */
    public AnimationManager(Route route, Runnable repaint, MapPanel mapPanel) {
        this.mapPanel = mapPanel;
        this.route = route;
        this.repaint = repaint;

        timer = new Timer(30, e -> {
            animationStep += 2;
            if (animationStep >= 100) {
                animationStep = 0;
                if (currentNode >= this.route.nodes().size()-1) {
                    System.out.println("Timer stopped for route: " + route.vehicle());
                    timer.stop();
                } else {
                    currentNode++;
                    Node thisNode = this.route.nodes().get(currentNode);
                    if (thisNode.isPickup()){ // set cargo size
                        cargo++;
                    } else {
                        cargo--;
                    }
                }
            }
            repaint.run();
        });
    }
    /**
     * Starts the animation
     * Resets the animationStep, currentNode and cargo variables
     */
    public void start() {
        animationStep = 0;
        currentNode = 0;
        cargo = 0;
        timer.start();
    }

    /**
     * Draws the animated line between two nodes
     * Interpolates the line using animationStep variable 
     * @param g2
     */
    public void drawAnimatedLine(Graphics2D g2) {
        start = route.nodes().get(currentNode);

        if (currentNode >= route.nodes().size() - 1) {
            g2.drawImage(mapPanel.getCargoImages().get(0),start.getX()-30, start.getY()-20,60,40, null);
            return; // No more nodes to animate
        }
        if (route.vehicle() == 0) {
            animateDummy(g2);
            return; // Skip animation for vehicle 0
        }

        end = route.nodes().get(currentNode + 1);
        renderLocation(g2);
    }

    /**
     * Draws the completed segments of the route
     * @param g2 Graphics2D object for drawing
     */
    public void drawCompletedSegments(Graphics2D g2) {
        if (route.vehicle() == 0) {
            drawDummySegments(g2);
            return;
        }
        int bounds = Math.min(currentNode, route.nodes().size() - 1); // if animation complete, draw up to the last node

        for (int i = 0; i < bounds; i++) {
            start = route.nodes().get(i);
            end = route.nodes().get(i + 1);
            g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
        drawCargo(g2);
        
        
    }

    /**
     * Draws cargo images
     * Draws cargo at pickup locations and delivery locations after visited
     * @param g2
     */
    private void drawCargo(Graphics2D g2) {
        List<Node> traveledRoute = route.nodes().subList(1,currentNode+1);
        List<Node> futureRoute = route.nodes().subList(currentNode +1, route.nodes().size());

        // draw future pickup cargo
        for (Node node: futureRoute){
            if (node.isPickup()){
                g2.drawImage(mapPanel.getCargoImages().get(0),node.getX()-30, node.getY()-20,60,40, null);
            }
        }
        if (currentNode == 0){
            return;
        }
        // draw delivered cargo
        for (Node node : traveledRoute){
            if (!node.isPickup()){
                g2.drawImage(mapPanel.getCargoImages().get(0),node.getX()-30, node.getY()-20,60,40, null);
            }
        }
    }

    /**
     * Draws the vehicle image at the given coordinates
     * @param g2
     * @param x
     * @param y
     */
    public void drawVehicle(Graphics2D g2, int x, int y){
        List<Image> vehicleImages = mapPanel.getVehicleImages();
        List<Image> cargoImages = mapPanel.getCargoImages();
        int vehicle = route.vehicle();

        if (cargo > 0){ // draw cargo if carried
            g2.drawImage(cargoImages.get(0),x-30,y-20,60,40,null);
        }
        g2.drawImage(vehicleImages.get(vehicle),x-30,y-20,60,40,null);
    }

    /**
     * Draws the animated line for the dummy vehicle
     * Skips the animation between different calls
     * @param g2
     */
    private void animateDummy(Graphics2D g2) {
        setDummyIndexes(currentNode);

        // skip to next call pair, no home node for dummy vehicle
        if (assignedCalls.get(startIndex) != assignedCalls.get(endIndex)){
            currentNode++;
            start = route.nodes().get(currentNode);
            end = route.nodes().get(currentNode + 1);
        }

        renderLocation(g2);
    }

    /**
     * Draws the completed segments of the route for the dummy vehicle
     * @param g2
     */
    private void drawDummySegments(Graphics2D g2) {
        int bounds = Math.min(currentNode, route.nodes().size() - 1); // if animation complete, draw up to the last node

        for (int i = 0; i < bounds; i++) {
            setDummyIndexes(i);

            // skip to next call pair, no home node for dummy vehicle
            if (assignedCalls.get(startIndex) != assignedCalls.get(endIndex)){
                continue;
            }
            g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }
    /**
     * Sets the start and end nodes for the dummy vehicle
     * @param node
     */
    private void setDummyIndexes(int node) {
        start = route.nodes().get(node);
        end = route.nodes().get(node + 1);
        startIndex = route.nodes().indexOf(start);
        endIndex = route.nodes().indexOf(end);
        assignedCalls = mapPanel.getSolution().get(route.vehicle());
    }
    /**
     * Interpolates the line between the start and end nodes
     * @param g2
     */
    private void renderLocation(Graphics2D g2) {
        // interpolate the line
        int currentX = start.getX() + (end.getX() - start.getX()) * animationStep / 100;
        int currentY = start.getY() + (end.getY() - start.getY()) * animationStep / 100;

        drawVehicle(g2, currentX, currentY);
        g2.drawLine(start.getX(), start.getY(), currentX, currentY);
    }

    @Override
    public String toString() {
        return "AnimationManager{" +
                "timer=" + timer +
                ", route=" + route +
                ", animationStep=" + animationStep +
                ", repaint=" + repaint +
                ", currentNode=" + currentNode +
                '}'; 
    }

    public void setRoute(Route route) {
        this.route = route;
        this.animationStep = 0;
        this.currentNode = 0;
        System.out.println("Route set to: " + route);
    }
    public Route getRoute() {
        return route;
    }
    public int getAnimationStep() {
        return animationStep;
    }
    public int getCurrentNode() {
        return currentNode;
    }
}
