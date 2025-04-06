package no.uib.inf101.sample.utils.feasibility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import no.uib.inf101.sample.parser.Read;

public class Feasible {
    Read read;

    public Feasible(Read read){
        this.read = read;
    }

    /**
     * Check the feasibility of a solution
     * @param Hashmap solution
     * @return boolean feasible
     * @throws IOException
     */
    public boolean checkFeasibility(HashMap<Integer, List<Integer>> solution) throws IOException{
        Capacity capacity = new Capacity(read);
        CheckTimeWindow checkTimeWindow = new CheckTimeWindow(read);
        if (!capacity.checkCapacity(solution)) {
            return false;
        }
        if (!checkTimeWindow.checkAllTimeWindows(solution)) {
            return false;
        }
    return true;
    }

}
