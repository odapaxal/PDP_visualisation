import no.uib.inf101.sample.Main;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;

public class RunClickListener implements java.awt.event.ActionListener {
    private final MapPanel mapPanel;

    public RunClickListener(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Handle the button click event here
        System.out.println("Run button clicked!");
        
        Main main = new Main();
        HashMap<Integer, List<Integer> solution = main.run(read);
        setSolution(solution);
        mapPanel.repaint();
    }
}