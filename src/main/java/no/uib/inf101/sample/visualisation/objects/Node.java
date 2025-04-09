package no.uib.inf101.sample.visualisation.objects;

public record Node(int id, int x, int y){
    
    @Override
    public final String toString() {
        return "Node " + id + " (" + x + ", " + y + ")";
    }
};
