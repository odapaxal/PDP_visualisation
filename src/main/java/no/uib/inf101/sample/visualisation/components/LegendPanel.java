package no.uib.inf101.sample.visualisation.components;

import java.awt.Graphics;

import javax.swing.JPanel;

public class LegendPanel extends JPanel{
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Test", 100, 100);
    }
}
