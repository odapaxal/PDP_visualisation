package no.uib.inf101.sample.objects;

import java.util.List;

public record SolutionAndCost(int vehicle, List<Integer> assignedCalls, int cost) {
    
}
