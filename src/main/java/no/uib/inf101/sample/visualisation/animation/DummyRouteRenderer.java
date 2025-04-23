package no.uib.inf101.sample.visualisation.animation;

import java.awt.Graphics2D;
import java.util.List;

import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;

public class DummyRouteRenderer extends RouteRenderer {
    private int startIndex;
    private int endIndex;
    private List<Integer> assignedCalls;
    private List<Node> nodes;

    /**
     * Creates a RouteRenderer to visualise the dummy routes. 
     * They are drawn as individual lines between nodes.
     * @param route
     * @param mapPanel
     * @param animationManager
     */
    public DummyRouteRenderer(Route route, MapPanel mapPanel, AnimationManager animationManager) {
        super(route, mapPanel, animationManager);
        update();
    }

    /**
     * Sets the route and updates the nodes and assigned calls.
     * @param route to be animated
     */
    @Override
    public void setRoute(Route route) {
        super.setRoute(route); 
        update();
    }

    private void update() {
        nodes = this.route.nodes();
        assignedCalls = this.mapPanel.getSolution().get(0);
    }

    @Override
    protected void drawAnimatedLine(Graphics2D g2) {
        int currentNode = animationManager.getCurrentNode();
        Node start = nodes.get(currentNode);
        
        if (currentNode >= nodes.size() - 1){ // if last node, place cargo image
            g2.drawImage(mapPanel.getCargoImages().get(0),start.getX()-30, start.getY()-20,60,40, null);
            return;
        } 

        Node end = nodes.get(currentNode + 1);
        isSameCallPair(start, end); // set startIndex and endIndex

        // skip to next call pair, no home node for dummy vehicle
        if (assignedCalls.get(startIndex) != assignedCalls.get(endIndex)){
            currentNode++;
            start = nodes.get(currentNode);
            end = nodes.get(currentNode + 1);
        }
        drawAnimatedVehicle(g2, animationManager.getAnimationStep(), start, end);
    }

    @Override
    protected void drawCompletedSegments(Graphics2D g2) {
        int currentNode = animationManager.getCurrentNode();
        int bounds = Math.min(currentNode, nodes.size() - 1);

        // draw each segment individually
        for (int i = 0; i < bounds; i++) {
            Node start = nodes.get(i);
            Node end = nodes.get(i + 1);

            if (!isSameCallPair(start, end)) continue;

            g2.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }

    private boolean isSameCallPair(Node start, Node end) {
        startIndex = nodes.indexOf(start);
        endIndex = nodes.indexOf(end);
        return assignedCalls.get(startIndex).equals(assignedCalls.get(endIndex));
    }
}
