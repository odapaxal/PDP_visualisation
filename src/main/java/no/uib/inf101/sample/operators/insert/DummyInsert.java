package no.uib.inf101.sample.operators.insert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.solver.ALNS;


public class DummyInsert extends InsertOperator{
    //Insert all calls to the dummy

    public DummyInsert(Read read) {
        super(read);
    }

    @Override
    public HashMap<Integer, List<Integer>> operate(HashMap<Integer, List<Integer>> newSolution,
            List<Integer> callsToInsert, HashMap<Integer,List<Integer>> previousSolution) throws IOException {
        HashMap<Integer, List<Integer>> solution = ALNS.deepCopy(newSolution);
        List<Integer> dummyRoute = solution.get(0);
        
        for (int call : callsToInsert) {
            dummyRoute.add(call);
            dummyRoute.add(call);
        }
        solution.put(0, dummyRoute);
        return solution;
    }    
    
}
