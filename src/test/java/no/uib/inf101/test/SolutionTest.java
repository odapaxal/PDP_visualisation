package no.uib.inf101.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.utils.Solution;


public class SolutionTest {
    Read read;
    Solution solver;

    public SolutionTest(){
        try {
            read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
            solver = new Solution(read);
            solver.run();
        } catch (IOException e) {
            e.printStackTrace();
            read = null; // Handle initialization failure
        }
    }
    @Test
    void testGetSolution() {
        int numVehicles = read.getNumVehicles();
        int numCalls = read.getNumCalls();

        HashMap<Integer, List<Integer>> solution = solver.getSolution();
        
        int solutionSize = solution.values().stream()
            .mapToInt(List::size)
            .sum();
    
        // Check that solution contains the correct total amount of calls
        assertEquals(solutionSize, numCalls*2, "The amount of calls in solution does not match expected number of calls: "+solutionSize);

        // Check that solution contains the correct number of vehicles
        assertEquals(solution.size(), numVehicles+1, "The solution size does not match number of vehicles plus dummy vehicle"); 

        // Check that there are two occurrences for each call in the solution
        for (int i = 0; i < numCalls; i++) {
            int callId = i + 1; // Call IDs are 1-indexed
            int count = 0;
            for (List<Integer> route : solution.values()) {
                count += route.stream().filter(id -> id == callId).count();
            }
            assertEquals(count, 2, "Call ID " + callId + " does not appear exactly twice in the solution.");
        }

    }
}
