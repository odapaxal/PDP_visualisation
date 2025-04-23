package no.uib.inf101.sample.visualisation.objects;

public class Node {
    private int id;
    private int x;
    private int y;
    private boolean pickup;

    /**
     * Creates a node with the given id, x and y coordinates, and pickup status.
     * @param id
     * @param x
     * @param y
     * @param pickup
     */
    public Node(int id, int x, int y, boolean pickup) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.pickup = pickup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    @Override
    public String toString() {
        return "Node " + id + " (" + x + ", " + y + "), pickup: " + pickup;
    }
}
