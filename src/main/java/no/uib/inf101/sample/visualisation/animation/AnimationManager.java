package no.uib.inf101.sample.visualisation.animation;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import javax.swing.Timer;

import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class AnimationManager {
    private Timer timer; //check if needs to be final
    private final Route route;
    private int animationStep = 0;
    private final Runnable repaint;
    private int currentNode = 0;

    /**
     * Constructor for AnimationManager
     * @param route
     * @param repaint
     * Starts a new timer for animating the route
     */
    public AnimationManager(Route route, Runnable repaint) {
        this.route = route;
        this.repaint = repaint;

        timer = new Timer(20, e -> {
            animationStep += 2;
            if (animationStep >= 100) {
                animationStep = 0;
                currentNode++;
                if (currentNode >= route.nodes().size()) {
                    timer.stop();
                }
            }
            System.out.println("Calling repaint, animationStep: " + animationStep + ", currentNode: " + currentNode);
            repaint.run();
        });
    }

    public void start() {
        animationStep = 0;
        currentNode = 0;
        timer.start();
    }

    public void drawAnimatedLine(Graphics2D g2) {
        if (currentNode >= route.nodes().size() - 1) {
            return; // No more nodes to animate
        }

        Node start = route.nodes().get(currentNode);
        Node end = route.nodes().get(currentNode + 1);

        // interpolate the line
        int currentX = start.x() + (end.x() - start.x()) * animationStep / 100;
        int currentY = start.y() + (end.y() - start.y()) * animationStep / 100;

        g2.setColor(route.color());
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(start.x(), start.y(), currentX, currentY);
    }

    public void drawCompletedSegments(Graphics2D g2){
        g2.setColor(route.color());
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < currentNode; i++) {
            Node start = route.nodes().get(i);
            Node end = route.nodes().get(i + 1);
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
}
