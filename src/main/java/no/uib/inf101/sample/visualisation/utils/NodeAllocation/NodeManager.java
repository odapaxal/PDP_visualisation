package no.uib.inf101.sample.visualisation.utils.NodeAllocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.uib.inf101.sample.objects.Call;
import no.uib.inf101.sample.objects.Vehicle;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.objects.Node;

public class NodeManager {
    Read read;
    private List<Node> nodes;
    private List<Node> relevantNodes;
    
    /**
     * Constructor for NodeManager
     * Creates a list of all nodes, and sets coordinates (0,0) for the relevant nodes for the data
     * @param read
     */
    NodeManager(Read read) {
        this.read = read;
        createNodeList();
        addRelevantNodes();
    }

    /**
     * Initialises a node list with (0,0) coordinates for all
     */
    private void createNodeList(){
        //random node allocation
        nodes = new ArrayList<>();
        for (int i = 1; i <= read.getNumNodes(); i++) {
            nodes.add(new Node(i, 0, 0, false));
        }
    }

    /**
     * Creates a new list containing only the nodes that are relevant for the data set
     */
    private void addRelevantNodes() {
        relevantNodes = new ArrayList<>();

        // add vehicle home nodes
        List<Vehicle> vehicles = read.getVehicles();
        for (int i = 0; i < vehicles.size(); i++) {
            int homeNode = vehicles.get(i).getHomeNode();
            relevantNodes.add(nodes.get(homeNode-1));
        }
        // add call origin and destination nodes
        List<Call> calls = read.getCalls();
        for (int i = 0; i < read.getNumCalls(); i++) {
            int originNode = calls.get(i).getOriginNode();
            int destinationNode = calls.get(i).getDestinationNode();
            relevantNodes.add(nodes.get(originNode-1));
            relevantNodes.add(nodes.get(destinationNode-1));
        }
    }
    public List<Node> getRelevantNodes() {
        return Collections.unmodifiableList(relevantNodes);
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }
}
