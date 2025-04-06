package no.uib.inf101.sample.operators.removal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import no.uib.inf101.sample.parser.Read;

//Super random operator
public class RemovalOperator3 extends RemovalOperator{
    Random random = new Random();
    int allCalls;

    public RemovalOperator3(Read read) {
        super(read);
        allCalls = read.getNumCalls();
    }

    @Override
    public List<Integer> chooseCalls(HashMap<Integer, List<Integer>> solution, int k) throws IOException {
        int solutionSize = solution.values().stream().mapToInt(List::size).sum();
        List<Integer> callsToRemove = new ArrayList<>();

        // Choose k random calls to remove
        while (callsToRemove.size() < k) {
            int randomCall = random.nextInt(allCalls) + 1;
            if (!callsToRemove.contains(randomCall)) {
                callsToRemove.add(randomCall);
            } else if (solutionSize == callsToRemove.size() * 2) {
                return callsToRemove;
            }
        }
        if (callsToRemove.isEmpty()){
            throw new IOException("No calls to remove for solution:"+solution+" with size: "+solutionSize);
        }
        return callsToRemove;
    }
    
}
