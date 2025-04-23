package no.uib.inf101.sample.visualisation.events;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.utils.Solution;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class RunClickListener implements java.awt.event.ActionListener {
    private final MapPanel mapPanel;
    private LegendPanel legendPanel;
    Read read;

    /**
     * Creates a RunClickListener to run the Adaptive Local Neighbourhood Search Algorithm
     * when the button is clicked.
     * @param legendPanel
     * @param mapPanel
     */
    public RunClickListener(LegendPanel legendPanel, MapPanel mapPanel) {
        this.legendPanel = legendPanel;
        this.mapPanel = mapPanel;
        read = mapPanel.getRead();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        SwingUtilities.invokeLater(() -> legendPanel.setMessage("Running Adaptive Local Neighbourhood Search Algorithm..."));

        // Run the solution in a background thread
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Solution solution = new Solution(read);
                solution.run(); 
                mapPanel.setSolution(solution.getSolution());
                return null;
            }

            @Override
            protected void done() {
                // Update the UI after run completes
                SwingUtilities.invokeLater(() -> {
                    legendPanel.setMessage("Solution completed!");
                    mapPanel.startAnimation();
                });
            }
        }.execute();
    }
}