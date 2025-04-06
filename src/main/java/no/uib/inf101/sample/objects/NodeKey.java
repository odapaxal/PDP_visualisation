package no.uib.inf101.sample.objects;
import java.util.Objects;

public class NodeKey {
    private final int vehicle;
    private final int call;

    /**
     * 
     * @param vehicle
     * @param call
     */
    public NodeKey(int vehicle, int call) {
        this.vehicle = vehicle;
        this.call = call;
    }

    public int getVehicle() {
        return vehicle;
    }

    public int getCall() {
        return call;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeKey nodeKey = (NodeKey) o;
        return vehicle == nodeKey.vehicle && call == nodeKey.call;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicle, call);
    }
}
