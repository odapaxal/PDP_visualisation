package no.uib.inf101.sample.visualisation.animation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class RouteRenderer {
    protected Route route;
    protected final MapPanel mapPanel;
    protected final AnimationManager animationManager;
    private List<Node> nodes;

    /**
     * Creates a RouteRenderer to visualise the route.
     * 
     * @param route to be animated
     * @param mapPanel
     * @param animationManager
     */
    public RouteRenderer(Route route, MapPanel mapPanel, AnimationManager animationManager) {
        this.route = route;
        this.mapPanel = mapPanel;
        this.animationManager = animationManager;
    }

    public void draw(Graphics2D g2) {
        drawCompletedSegments(g2);
        drawAnimatedLine(g2);
    }

    protected void drawCompletedSegments(Graphics2D g2) {
        int currentNode = animationManager.getCurrentNode();
        int bounds = Math.min(currentNode, nodes.size() - 1);

        for (int i = 0; i < bounds; i++) {
            Node start = nodes.get(i);
            Node end = nodes.get(i + 1);
            g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
        drawCargo(g2);
    }

    protected void drawAnimatedLine(Graphics2D g2) {
        int currentNode = animationManager.getCurrentNode();
        int animationStep = animationManager.getAnimationStep();

        Node start = nodes.get(currentNode);

        if (currentNode >= nodes.size() - 1){
            g2.drawImage(mapPanel.getCargoImages().get(0),start.getX()-30, start.getY()-20,60,40, null);
            return;
        } 
        Node end = nodes.get(currentNode + 1);

        drawAnimatedVehicle(g2, animationStep, start, end);
    }

    protected void drawAnimatedVehicle(Graphics2D g2, int animationStep, Node start, Node end) {
        int currentX = start.getX() + (end.getX() - start.getX()) * animationStep / 100;
        int currentY = start.getY() + (end.getY() - start.getY()) * animationStep / 100;

        drawVehicle(g2, currentX, currentY);
        g2.drawLine(start.getX(), start.getY(), currentX, currentY);
    }

    protected void drawCargo(Graphics2D g2) {
        int currentNode = animationManager.getCurrentNode();
        List<Node> traveledRoute = nodes.subList(1, currentNode + 1);
        List<Node> futureRoute = nodes.subList(currentNode + 1, nodes.size());

        for (Node node : futureRoute) {
            if (node.isPickup()) {
                g2.drawImage(mapPanel.getCargoImages().get(0), node.getX() - 30, node.getY() - 20, 60, 40, null);
            }
        }

        for (Node node : traveledRoute) {
            if (!node.isPickup()) {
                g2.drawImage(mapPanel.getCargoImages().get(0), node.getX() - 30, node.getY() - 20, 60, 40, null);
            }
        }
    }

    protected void drawVehicle(Graphics2D g2, int x, int y) {
        List<Image> vehicleImages = mapPanel.getVehicleImages();
        List<Image> cargoImages = mapPanel.getCargoImages();
        int vehicle = route.vehicle();

        if (animationManager.getCargo() > 0 || vehicle == 0) {
            g2.drawImage(cargoImages.get(0), x - 30, y - 20, 60, 40, null);
        }
        g2.drawImage(vehicleImages.get(vehicle), x - 30, y - 20, 60, 40, null);
    }

    public void setRoute(Route route) {
        this.route = route;
        this.nodes = route.nodes();
    }

    public Route getRoute() {
        return route;
    }
}
