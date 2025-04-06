package no.uib.inf101.sample.operators.removal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import no.uib.inf101.sample.operators.Operator;
import no.uib.inf101.sample.parser.Read;


public abstract class RemovalOperator extends Operator {

    public RemovalOperator(Read read) {
        super(read);
    }

    /**
     * Choose calls to remove from solution
     * @param vehicle
     * @param list of assigned calls to vehicle 
     * @return list of calls to remove
     * @throws IOException
     */
    public abstract List<Integer> chooseCalls(HashMap<Integer,List<Integer>> solution, int k) throws IOException;

}    
     

