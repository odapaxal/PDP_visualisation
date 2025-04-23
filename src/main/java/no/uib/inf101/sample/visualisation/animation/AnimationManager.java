package no.uib.inf101.sample.visualisation.animation;

import java.awt.Graphics2D;
import javax.swing.Timer;
import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Route;

public class AnimationManager {
    private Timer timer;
    private Route route;
    private int animationStep = 0;
    private int cargo = 0;
    private int currentNode = 0;
    private final Runnable repaint;
    private final RouteRenderer routeRenderer;

    /**
     * AnimationManager that visualises the route of a vehicle
     * @param route 
     * @param repaint
     * @param mapPanel
     */
    public AnimationManager(Route route, Runnable repaint, MapPanel mapPanel) {
        this.route = route;
        this.repaint = repaint;

        // Choose the appropriate renderer based on the vehicle type
        if (route.vehicle() == 0) {
            this.routeRenderer = new DummyRouteRenderer(route, mapPanel, this);
        } else {
            this.routeRenderer = new RouteRenderer(route, mapPanel, this);
        }

        initializeTimer();
    }

    private void initializeTimer() {
        timer = new Timer(30, e -> {
            animationStep += 2;
            if (animationStep >= 100) {
                animationStep = 0;
                if (currentNode >= route.nodes().size() - 1) {
                    timer.stop();
                } else {
                    currentNode++;
                    if (route.nodes().get(currentNode).isPickup()) {
                        cargo++;
                    } else {
                        cargo--;
                    }
                }
            }
            repaint.run();
        });
    }

    /** Stars animation and resets animationstep, currentnode and cargo */
    public void start() {
        animationStep = 0;
        currentNode = 0;
        cargo = 0;
        timer.start();
    }

    public void draw(Graphics2D g2) {
        routeRenderer.draw(g2);
    }

    public int getAnimationStep() {
        return animationStep;
    }

    public int getCurrentNode() {
        return currentNode;
    }

    public int getCargo() {
        return cargo;
    }

    public Route getRoute() {
        return route;
    }
    public RouteRenderer getRouteRenderer() {
        return routeRenderer;
    }

    /**
     * Sets the route and resets animation step and current node
     * @param route
     */
    public void setRoute(Route route) {
        this.route = route;
        this.animationStep = 0;
        this.currentNode = 0;
        routeRenderer.setRoute(route);
    }

}
