package no.uib.inf101.sample.visualisation.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.events.RunClickListener;

public class LegendPanel extends JPanel{
    public MapPanel mapPanel;
    private JLabel messageLabel;
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

        // output message label
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);

        // solve button
        JButton solveButton = new JButton();
        initializeSolveButton(solveButton);

        // add components
        add(Box.createVerticalGlue());
        add(messageLabel);
        add(Box.createVerticalStrut(10));
        add(solveButton);
        add(Box.createVerticalGlue());
    }

    private void initializeSolveButton(JButton solveButton) {
        solveButton.setText("Solve");
        solveButton.setPreferredSize(new Dimension(100, 50));
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(new RunClickListener(this,mapPanel));
        solveButton.setFont(getFont());
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

    /**
     * Sets the message to be displayed in the legend panel
     * @param message
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
        repaint();
    }
}
