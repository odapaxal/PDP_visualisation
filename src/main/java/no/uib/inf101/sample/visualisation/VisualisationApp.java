package no.uib.inf101.sample.visualisation;

import java.awt.FlowLayout;

import javax.swing.*;

import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;

public class VisualisationApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Create main frame
            JFrame frame = new JFrame("Visualisation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);

            MapPanel mapPanel = new MapPanel();
            LegendPanel legendPanel = new LegendPanel();
            
            // Add components to the frame
            frame.setLayout(new FlowLayout(FlowLayout.CENTER));
            frame.add(mapPanel, "Center");
            //frame.add(legendPanel);

            frame.setVisible(true);
        });
    }
}
