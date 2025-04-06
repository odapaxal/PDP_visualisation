package no.uib.inf101.sample.objects;

public class NodeInfo {
    private int vehicleIndex;
    private int callIndex;
    private int originNodeTime;
    private int originNodeCost;
    private int destNodeTime;
    private int destNodeCost;

    public NodeInfo(int vehicleIndex, int callIndex, int originNodeTime, int originNodeCost, int destNodeTime, int destNodeCost){
        this.vehicleIndex = vehicleIndex;
        this.callIndex = callIndex;
        this.originNodeTime = originNodeTime;
        this.originNodeCost = originNodeCost;
        this.destNodeTime = destNodeTime;
        this.destNodeCost = destNodeCost;
    }
    public int getVehicleIndex(){return vehicleIndex;}
    public int getCallIndex(){return callIndex;}
    public int getOriginNodeTime() {return originNodeTime;}
    public int getOriginNodeCost() {return originNodeCost;}
    public int getDestNodeTime() {return destNodeTime;}
    public int getDestNodeCost() {return destNodeCost;}

    @Override
    public String toString() {
        return "\n Nodeinfo{" +
                " \n vehicleIndex=" + vehicleIndex +
                ", \n callindex=" + callIndex +
                ", \n originNodeTime=" + originNodeTime +
                ", \n originNodeCost=" + originNodeCost +
                ", \n destNodeTime=" + destNodeTime +
                ", \n destNodeCost="+ destNodeCost+
                '}';
    }
}
