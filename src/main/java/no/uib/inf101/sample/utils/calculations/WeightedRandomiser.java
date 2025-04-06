package no.uib.inf101.sample.utils.calculations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightedRandomiser {
    private final int[] keys;             // Array of indices (keys)
    private final int[] cumulativeWeights; // Cumulative weights corresponding to keys
    private final int totalWeight;
    private final Random random;

    /**
     * Constructor for the WeightedRandomiser class.
     * 
     * @param weightMap A map of indices (keys) to their corresponding weights.
     */
    public WeightedRandomiser(HashMap<Integer, Integer> weightMap) {
        int size = weightMap.size();
        keys = new int[size];
        cumulativeWeights = new int[size];

        int index = 0;
        int cumulativeSum = 0;
        // Iterate over the map entries.
        for (Map.Entry<Integer, Integer> entry : weightMap.entrySet()) {
            keys[index] = entry.getKey();
            cumulativeSum += entry.getValue();
            cumulativeWeights[index] = cumulativeSum;
            index++;
        }
        totalWeight = cumulativeSum;
        random = new Random();
    }

    /**
     * Selects and returns a random index based on its weight.
     */
    public int next() {
        // Generate a random integer between 0 (inclusive) and totalWeight (exclusive)
        if(totalWeight <= 0){
            throw new IllegalArgumentException("Total weight must be greater than 0: " + totalWeight 
                + " for weightmap: " + Arrays.toString(cumulativeWeights));
        }        
        int r = random.nextInt(totalWeight);
        // Adjust by 1 so that boundaries fall correctly.
        int searchValue = r + 1;
        // Binary search for the first cumulative weight >= searchValue.
        int idx = Arrays.binarySearch(cumulativeWeights, searchValue);
        if (idx < 0) {
            idx = -idx - 1;
        }
        return keys[idx];
    }
}
