package no.uib.inf101.sample.objects;

public class Call {
    private int index;
    private int originNode;
    private int destinationNode;
    private int size;
    private int costOfNotTransporting;
    private int pickupLowerTimeWindow;
    private int pickupUpperTimeWindow;
    private int deliveryLowerTimeWindow;
    private int deliveryUpperTimeWindow;

    public Call(int index, int originNode, int destinationNode, int size, int costOfNotTransporting,
                int pickupLowerTimeWindow, int pickupUpperTimeWindow, int deliveryLowerTimeWindow, int deliveryUpperTimeWindow) {
        this.index = index;
        this.originNode = originNode;
        this.destinationNode = destinationNode;
        this.size = size;
        this.costOfNotTransporting = costOfNotTransporting;
        this.pickupLowerTimeWindow = pickupLowerTimeWindow;
        this.pickupUpperTimeWindow = pickupUpperTimeWindow;
        this.deliveryLowerTimeWindow = deliveryLowerTimeWindow;
        this.deliveryUpperTimeWindow = deliveryUpperTimeWindow;
    }

    public int getIndex() { return index; }
    public int getOriginNode() { return originNode; }
    public int getDestinationNode() { return destinationNode; }
    public int getSize() { return size; }
    public int getCostOfNotTransporting() { return costOfNotTransporting; }
    public int getPickupLowerTimeWindow() { return pickupLowerTimeWindow; }
    public int getPickupUpperTimeWindow() { return pickupUpperTimeWindow; }
    public int getDeliveryLowerTimeWindow() { return deliveryLowerTimeWindow; }
    public int getDeliveryUpperTimeWindow() { return deliveryUpperTimeWindow; }

    @Override
    public String toString() {
        return "Call{" +
                "index=" + index +
                ", originNode=" + originNode +
                ", destinationNode=" + destinationNode +
                ", size=" + size +
                ", costOfNotTransporting=" + costOfNotTransporting +
                ", pickupLowerTimeWindow=" + pickupLowerTimeWindow +
                ", pickupUpperTimeWindow=" + pickupUpperTimeWindow +
                ", deliveryLowerTimeWindow=" + deliveryLowerTimeWindow +
                ", deliveryUpperTimeWindow=" + deliveryUpperTimeWindow +
                '}';
    }
}
