package no.uib.inf101.sample.visualisation.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JPanel;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.events.RunClickListener;

public class LegendPanel extends JPanel{
    Read read;
    int width = 1000;
    int height = 100;

    public LegendPanel(Read read){
        this.read = read;
        setPreferredSize(new Dimension(width, height));
        setFont(new Font("CourierNew", Font.PLAIN, 15));

        JButton solveButton = new JButton();
        initializeSolveButton(solveButton);
    }

    private void initializeSolveButton(JButton solveButton) {
        solveButton.setText("Solve");
        solveButton.setPreferredSize(new Dimension(100, 50));
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(new RunClickListener(this));
        solveButton.setFont(getFont());
        solveButton.addMouseListener(setMouseListener(solveButton));

        add(solveButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Graphics2D g2 = (Graphics2D) g;
        
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

    public Read getRead() {
        return read;
    }
}
