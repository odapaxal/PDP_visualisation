package no.uib.inf101.sample.visualisation.events;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.utils.Solution;


public class RunClickListener implements java.awt.event.ActionListener {
    private final LegendPanel panel;
    Read read;

    public RunClickListener(LegendPanel panel) {
        this.panel = panel;
        read = panel.getRead();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Solution solution = new Solution(read);
        try {
            solution.run();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // call the paintComponent method of the MapPanel
        MapPanel mapPanel = new MapPanel(read);
        mapPanel.setSolution(solution.getSolution());
        mapPanel.startAnimation();
    }
}