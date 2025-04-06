package no.uib.inf101.sample.visualisation.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import no.uib.inf101.sample.visualisation.utils.CoordinateTransformer;
import no.uib.inf101.sample.visualisation.utils.Node;

public class MapPanel extends JPanel{
    int width = 1000;
    int height = 600;

    public MapPanel() {
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(new Color(135, 206, 235)); // Sky blue color
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawNodes(g2);
    }
    
    private void drawNodes(Graphics2D g2) {
        List<Node> nodes = new ArrayList<>();
        CoordinateTransformer transformer = new CoordinateTransformer(width, height);
        nodes = transformer.createNodeList();
        System.out.println("Nodes: " + nodes);
        for (Node node : nodes) {
            int x = node.x();
            int y = node.y();
            g2.setColor(Color.RED);
            g2.fillOval(x, y, 10, 10); // Draw each node as a small circle
            g2.drawString("Node " + node.id(), x + 12, y); // Label the node
        }
    }
}
