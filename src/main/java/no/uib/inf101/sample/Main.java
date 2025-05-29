package no.uib.inf101.sample;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;

import javax.swing.*;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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

            // header panel
            JPanel headerPanel = new JPanel();
            JLabel headerLabel = new JLabel("Pickup and Delivery Problem Visualisation");
            headerLabel.setFont(new Font("CourierNew", Font.BOLD, 20));
            headerPanel.add(headerLabel);

            // Add components to the frame
            frame.setLayout(new BorderLayout());
            frame.add(headerPanel, BorderLayout.NORTH); 
            frame.add(mapPanel, BorderLayout.CENTER);   
            frame.add(legendPanel, BorderLayout.SOUTH); 

            frame.setVisible(true);
        });
    }
}