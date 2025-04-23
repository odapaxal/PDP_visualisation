package no.uib.inf101.sample;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.*;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Create main frame
            JFrame frame = new JFrame("Visualisation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);
            
            String filepath = "src/main/resources/Call_7_Vehicle_3.txt";
            Read read = null;
            try {
                read = new Read(filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            MapPanel mapPanel = new MapPanel(read, null);
            LegendPanel legendPanel = new LegendPanel(read, mapPanel);
            mapPanel.legendPanel = legendPanel;
            
            // Add components to the frame
            frame.setLayout(new FlowLayout(FlowLayout.CENTER));
            frame.add(mapPanel, "Center");
            frame.add(legendPanel, "Center");

            frame.setVisible(true);
        });
    }


}