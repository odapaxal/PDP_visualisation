package no.uib.inf101.sample.objects;

public class Vehicle {
    private int index;
    private int homeNode;
    private int startTime;
    private int capacity;

    public Vehicle(int index, int homeNode, int startTime, int capacity) {
        this.index = index;
        this.homeNode = homeNode;
        this.startTime = startTime;
        this.capacity = capacity;
    }

    public int getIndex() { return index; }
    public int getHomeNode() { return homeNode; }
    public int getStartTime() { return startTime; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return "Vehicle{" +
                "index=" + index +
                ", homeNode=" + homeNode +
                ", startTime=" + startTime +
                ", capacity=" + capacity +
                '}';
    }
}
