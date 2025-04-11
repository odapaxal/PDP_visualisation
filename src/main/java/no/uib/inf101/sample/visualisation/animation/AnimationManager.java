package no.uib.inf101.sample.visualisation.animation;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.Timer;

import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class AnimationManager {
    private Timer timer; //check if needs to be final
    private Route route;
    private int animationStep = 0;
    private final Runnable repaint;
    private int currentNode = 0;
    private HashMap<Integer, List<Integer>> solution;
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

        timer = new Timer(20, e -> {
            animationStep += 2;
            if (animationStep >= 100) {
                animationStep = 0;
                if (currentNode >= this.route.nodes().size()) {
                    System.out.println("Timer stopped for route: " + route.vehicle());
                    timer.stop();
                } else {
                    currentNode++;
                }
            }
            //System.out.println("Calling repaint, animationStep: " + animationStep + ", currentNode: " + currentNode);
            repaint.run();
        });
    }

    public void start() {
        animationStep = 0;
        currentNode = 0;
        timer.start();
    }

    /**
     * Draws the animated line between two nodes
     * Interpolates the line using animationStep variable 
     * @param g2
     */
    public void drawAnimatedLine(Graphics2D g2) {
        if (currentNode >= route.nodes().size() - 1) {
            System.out.println("Animation completed for route: " + route.vehicle());
            return; // No more nodes to animate
        }
        if (route.vehicle() == 0) {
            animateDummy(g2);
            return; // Skip animation for vehicle 0
        }

        start = route.nodes().get(currentNode);
        end = route.nodes().get(currentNode + 1);

        // interpolate the line
        int currentX = start.x() + (end.x() - start.x()) * animationStep / 100;
        int currentY = start.y() + (end.y() - start.y()) * animationStep / 100;

        g2.setColor(route.color());
        g2.drawLine(start.x(), start.y(), currentX, currentY);
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

        g2.setColor(route.color());
        int bounds = Math.min(currentNode, route.nodes().size() - 1); // if animation complete, draw up to the last node

        for (int i = 0; i < bounds; i++) {
            start = route.nodes().get(i);
            end = route.nodes().get(i + 1);
            g2.drawLine(start.x(), start.y(), end.x(), end.y());
        }
    }

    /**
     * Draws the animated line for the dummy vehicle
     * Skips the animation between different calls
     * @param g2
     */
    private void animateDummy(Graphics2D g2) {
        g2.setColor(route.color());
        start = route.nodes().get(currentNode);
        end = route.nodes().get(currentNode + 1);
        int startIndex = route.nodes().indexOf(start);
        int endIndex = route.nodes().indexOf(end);
        List<Integer> assignedCalls = mapPanel.getSolution().get(route.vehicle());

        // skip to next call pair, no home node for dummy vehicle
        if (assignedCalls.get(startIndex) != assignedCalls.get(endIndex)){
            currentNode++;
            start = route.nodes().get(currentNode);
            end = route.nodes().get(currentNode + 1);
        }

        // interpolate the line
        int currentX = start.x() + (end.x() - start.x()) * animationStep / 100;
        int currentY = start.y() + (end.y() - start.y()) * animationStep / 100;

        g2.drawLine(start.x(), start.y(), currentX, currentY);
    }

    /**
     * Draws the completed segments of the route for the dummy vehicle
     * @param g2
     */
    private void drawDummySegments(Graphics2D g2) {
        g2.setColor(route.color());
        int bounds = Math.min(currentNode, route.nodes().size() - 1); // if animation complete, draw up to the last node
        if (mapPanel.getSolution().get(route.vehicle()) == null){
            throw new Error("Solution is null for vehicle: " + route.vehicle()+
                    ", route: " + route+ " solution " + mapPanel.getSolution());
        }
        List<Integer> assignedCalls = mapPanel.getSolution().get(route.vehicle());

        for (int i = 0; i < bounds; i++) {
            start = route.nodes().get(i);
            end = route.nodes().get(i + 1);
            int startIndex = route.nodes().indexOf(start);
            int endIndex = route.nodes().indexOf(end);

            // skip to next call pair, no home node for dummy vehicle
            if (assignedCalls.get(startIndex) != assignedCalls.get(endIndex)){
                continue;
            }
            g2.drawLine(start.x(), start.y(), end.x(), end.y());
        }
    }

    @Override
    public String toString() {
        return "AnimationManager{" +
                "timer=" + timer +
                ", route=" + route +
                ", animationStep=" + animationStep +
                ", repaint=" + repaint +
                ", currentNode=" + currentNode +
                '}'; // Added toString method for debugging
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
