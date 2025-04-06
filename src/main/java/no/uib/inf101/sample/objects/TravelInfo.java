package no.uib.inf101.sample.objects;

public class TravelInfo {
    private int vehicleIndex;
    private int originNode;
    private int destinationNode;
    private int travelTime;
    private int travelCost;

    public TravelInfo(int vehicleIndex, int originNode, int destinationNode, int travelTime, int travelCost) {
        this.vehicleIndex = vehicleIndex;
        this.originNode = originNode;
        this.destinationNode = destinationNode;
        this.travelTime = travelTime;
        this.travelCost = travelCost;
    }

    public int getVehicleIndex() { return vehicleIndex; }
    public int getOriginNode() { return originNode; }
    public int getDestinationNode() { return destinationNode; }
    public int getTravelTime() { return travelTime; }
    public int getTravelCost() { return travelCost; }

    @Override
    public String toString() {
        return "TravelInfo{" +
                "vehicleIndex=" + vehicleIndex +
                ", originNode=" + originNode +
                ", destinationNode=" + destinationNode +
                ", travelTime=" + travelTime +
                ", travelCost=" + travelCost +
                '}';
    }
}


