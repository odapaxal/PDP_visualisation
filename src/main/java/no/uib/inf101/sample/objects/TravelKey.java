package no.uib.inf101.sample.objects;

import java.util.Objects;

public class TravelKey {
    private final int vehicle;
    private final int origin;
    private final int destination;
    
    public TravelKey(int vehicle, int origin, int destination) {
        this.vehicle = vehicle;
        this.origin = origin;
        this.destination = destination;
    }
    
    // Getters (if needed)
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelKey that = (TravelKey) o;
        return vehicle == that.vehicle &&
               origin == that.origin &&
               destination == that.destination;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(vehicle, origin, destination);
    }
    @Override
    public String toString() {
        String s = "vehicle: "+ vehicle+
                    "\norigin: " + origin+
                    "\ndestination: "+destination;
        return s;
    }
}
