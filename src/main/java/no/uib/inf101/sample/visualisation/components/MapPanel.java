package no.uib.inf101.sample.visualisation.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import no.uib.inf101.sample.visualisation.utils.CoordinateTransformer;
import no.uib.inf101.sample.visualisation.utils.Node;

public class MapPanel extends JPanel{
    int width = 1000;
    int height = 600;

    private List<Node> nodes;
    private int animationStep = 0;
    private Timer timer;
    private Node startNode, endNode;

    Random random = new Random();


    public MapPanel() {
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(new Color(135, 206, 235)); // Sky blue color

        CoordinateTransformer transformer = new CoordinateTransformer(width, height);
        nodes = transformer.createNodeList();

        // Choose random nodes for testing
        startNode = nodes.get(random.nextInt(nodes.size()));
        endNode = nodes.get(random.nextInt(nodes.size()));
        while (startNode == endNode) {
            endNode = nodes.get(random.nextInt(nodes.size()));
        }
        startAnimation();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawNodes(g2);
        drawRoute(g2, nodes, Color.RED);
        
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

    private void drawRoute(Graphics2D g2, List<Node> route, Color color){
        Iterator<Node> iterator = route.iterator();
        while(iterator.hasNext()){
            Node start = iterator.next();
            if(iterator.hasNext()){
                Node end = iterator.next();
                drawAnimatedLine(g2, start, end, color);
                // Draw the line between the two nodes
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(start.x(), start.y(), end.x(), end.y());
            }
        }
    }

    private void drawAnimatedLine(Graphics2D g2, Node start, Node end, Color color) {
        // interpolate the line
        int currentX = start.x() + (end.x() - start.x()) * animationStep / 100;
        int currentY = start.y() + (end.y() - start.y()) * animationStep / 100;

        g2.setColor(color);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(start.x(), start.y(), currentX, currentY);
    }

    /**
     * Starts the animation of the line between two nodes.
     * Delay: 20ms
     * Animation step: 2
     * The line will be drawn in 50 steps.
     */
    private void startAnimation() {
        timer = new Timer(20, e -> {
            animationStep += 2;
            if (animationStep >= 100) {
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }
}
