package no.uib.inf101.sample.visualisation.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.events.RunClickListener;
import no.uib.inf101.sample.visualisation.objects.Route;

public class LegendPanel extends JPanel{
    public MapPanel mapPanel;
    private JLabel messageLabel;
    private JPanel legendContainer;
    Read read;
    int width = 1000;
    int height = 100;

    public LegendPanel(Read read, MapPanel mapPanel){
        this.mapPanel = mapPanel;
        this.read = read;

        // panel properties
        setPreferredSize(new Dimension(width, height));
        setFont(new Font("CourierNew", Font.PLAIN, 15));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // legend info
        legendContainer = new JPanel();
        legendContainer.setLayout(new BoxLayout(legendContainer, BoxLayout.X_AXIS));
        legendContainer.setAlignmentX(CENTER_ALIGNMENT);
        legendContainer.setOpaque(false);
        createLegend(mapPanel.getRoutes());

        // output message label
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);

        // solve button
        JButton solveButton = new JButton();
        initializeSolveButton(solveButton);

        // add components
        add(Box.createVerticalGlue());
        add(legendContainer);
        add(Box.createVerticalStrut(10));
        add(messageLabel);
        add(Box.createVerticalStrut(10));
        add(solveButton);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }

    private void initializeSolveButton(JButton solveButton) {
        solveButton.setText("Solve");
        solveButton.setPreferredSize(new Dimension(100, 50));
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(new RunClickListener(this,mapPanel));
        solveButton.addMouseListener(setMouseListener(solveButton));
        solveButton.setAlignmentX(CENTER_ALIGNMENT);

        add(solveButton);
    }

    private MouseAdapter setMouseListener(JButton solveButton) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                solveButton.setForeground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                solveButton.setForeground(null);
            }
        };
    }

    private void createLegend(List<Route> routes) {
        legendContainer.removeAll(); // Clear existing legend items
        for (Route route : routes) {
            JPanel legendItem = createLegendItem(route);
            legendContainer.add(legendItem);
        }
        legendContainer.revalidate(); // Refresh the layout
        legendContainer.repaint(); // Redraw the container
    }

    private JPanel createLegendItem(Route route) {
        JPanel legendItem = new JPanel();
        legendItem.setLayout(new BoxLayout(legendItem, BoxLayout.X_AXIS));
        legendItem.setOpaque(false); // Transparent background

        // Colored dot
        JLabel colorDot = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                System.out.println("Color: " + route.color());
                g2.setColor(route.color());
                g2.fillOval(5, 5, 10, 10); // Draw a small circle
            }
        };
        colorDot.setPreferredSize(new Dimension(20, 20));

        // Route label
        JLabel routeLabel = new JLabel("Vehicle " + route.vehicle());
        routeLabel.setFont(new Font("CourierNew", Font.PLAIN, 12));

        // Add components to the legend item
        legendItem.add(colorDot);
        legendItem.add(Box.createHorizontalStrut(10)); // Spacing between dot and label
        legendItem.add(routeLabel);

        return legendItem;
    }

    /**
     * Sets the message to be displayed in the legend panel
     * @param message
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
        repaint();
    }
}
