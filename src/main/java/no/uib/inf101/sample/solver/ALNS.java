package no.uib.inf101.sample.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import no.uib.inf101.sample.objects.OperatorScore;
import no.uib.inf101.sample.operators.Operator;
import no.uib.inf101.sample.operators.insert.DummyInsert;
import no.uib.inf101.sample.operators.insert.GreedyInsert;
import no.uib.inf101.sample.operators.insert.InsertOperator;
import no.uib.inf101.sample.operators.removal.RemovalOperator;
import no.uib.inf101.sample.operators.removal.RemovalOperator1;
import no.uib.inf101.sample.operators.removal.RemovalOperator2;
import no.uib.inf101.sample.operators.removal.RemovalOperator3;
import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.utils.VehicleRouteFormatter;
import no.uib.inf101.sample.utils.calculations.ObjectiveFunction;
import no.uib.inf101.sample.utils.calculations.WeightedRandomiser;
import no.uib.inf101.sample.utils.feasibility.Feasible;


public class ALNS {
    Read read;
    private HashMap<Integer, List<Integer>> bestSolution = null;
    private HashMap<Integer, List<Integer>> currentSolution = null;
    private int bestObjectiveValue = -1;
    private int currentObjective = -1;
    private int previousObjective = -1;
    private int deltaE = 0;
    private int iterationSinceLastBest = 0;
    private int escapeCount;
    private HashSet<Integer> visitedSolutions = new HashSet<>();
    private Operator operator;
    private InsertOperator insertOperator;
    private RemovalOperator removalOperator;
    private HashMap<Integer, Integer> operatorUsage;
    private HashMap<Integer, Integer> keepOperatorScores;
    private double threshold;
    Random random = new Random();
    VehicleRouteFormatter format = new VehicleRouteFormatter();
    ObjectiveFunction objectiveFunction;
    Feasible feasibleSolution;
    


    public ALNS(Read read){
        this.read = read;      
        objectiveFunction = new ObjectiveFunction(read);
        feasibleSolution = new Feasible(read);
        operator = new Operator(read);
    }
    /**
     * Adaptive large neighbourhood search algorithm
     * @param int objectiveValue
     * @param list initalSolution
     * @param filepath
     * @throws IOException
     */
    public void alns(int objectiveValue, HashMap<Integer,List<Integer>> initalSolution) throws IOException{
        int initialObjective = objectiveValue;
        HashMap<Integer, List<Integer>> previousSolution = deepCopy(initalSolution);
        bestSolution = deepCopy(previousSolution);
        bestObjectiveValue = initialObjective;

        //create operator combinations
        List<OperatorScore> operators = createOperatorCombinations();

        //assign initial operator weights
        HashMap<Integer, Integer> operatorWeights = new HashMap<>(); 
        for (int i = 0; i < operators.size(); i++){
            operatorWeights.put(i, operators.get(i).getScore());
        }
        WeightedRandomiser randomiser = new WeightedRandomiser(operatorWeights);
      
        boolean escape = false;
        double r = 0.2;
        operatorUsage = new HashMap<>();
        keepOperatorScores = new HashMap<>();
        for (int i = 0; i < operators.size(); i++){
            keepOperatorScores.put(i, 1);
            operatorUsage.put(i, 1); 
        }

        //Run ALNS
        for (int i = 1; i <= 10000; i++){
            // Set new operator weights every 100 iterations
            if (i % 100 == 0 && !escape){
                randomiser = updateOperatorScores(operators, r, operatorUsage, keepOperatorScores, i);
            } 
            // Escape function
            if (iterationSinceLastBest >= 500){ //change score for normal operators
                iterationSinceLastBest = 0;
                escape = true;
            } else if (escapeCount > 20){ //escape for 20 iterations
                escape = false;
                escapeCount = 0;
                //reset operator scores
                for (int j = 0; j < operators.size(); j++){
                    keepOperatorScores.put(j, 1);
                    operatorUsage.put(j, 1); 
                }
            }

            //choose an operator according to weights
            OperatorScore currentOperators = operators.get(randomiser.next()); 
            int operatorIndex = operators.indexOf(currentOperators);
            removalOperator = (RemovalOperator) currentOperators.getOperator().get(0);
            
            if (!escape){
                insertOperator = (InsertOperator) currentOperators.getOperator().get(1);
            } else{
                insertOperator = new DummyInsert(read); 
            }

            //operate solution
            int k = random.nextInt(10) + 1;
            currentSolution = new HashMap<>(operator.operate(previousSolution, removalOperator, insertOperator, k));
            int totalCalls = currentSolution.values().stream().mapToInt(List::size).sum();
            int totalVehicles = currentSolution.size();

            //debugging
            if(totalCalls != read.getNumCalls()*2){
                throw new IllegalStateException("Total calls in solution: " + totalCalls + " should be: " + read.getNumCalls()*2+ " current solution: " + currentSolution);
            }
            if (totalVehicles != read.getNumVehicles()+1){
                throw new IllegalStateException("Total vehicles in solution: " + totalVehicles + " should be: " + (read.getNumVehicles()+1)+ " current solution: " + currentSolution);
            }


            //calculate delta E
            previousObjective = objectiveFunction.calculateTotal(previousSolution);
            currentObjective = objectiveFunction.calculateTotal(currentSolution);
            visitedSolutions.add(currentObjective);

            deltaE = currentObjective - previousObjective;

            // if feasible and new solution
            if(feasibleSolution.checkFeasibility(currentSolution) && deltaE != 0){     

                    //Record to record threshold of accepting a worse solution
                    int runsLeft = 10000 - i;
                    threshold = bestObjectiveValue + bestObjectiveValue*0.2*(runsLeft/10000.0);
                    if (escape){ //if escape, accept anything new
                        threshold = initialObjective;
                    }
                    
                    if (deltaE < 0){ //if improved solution
                        if (currentObjective < bestObjectiveValue){
                            bestObjectiveValue = currentObjective;
                            bestSolution = currentSolution;
                            keepOperatorScores.merge(operatorIndex, 4, Integer::sum);
                            iterationSinceLastBest = 0;
                        } else {
                            keepOperatorScores.merge(operatorIndex, 2, Integer::sum); 
                        }
                        previousSolution = currentSolution;
                    } else {
                        //accept worse solution with threshold
                        if (currentObjective < threshold){
                            previousSolution = currentSolution;
                        } 
                        if (!visitedSolutions.contains(currentObjective)){
                            keepOperatorScores.merge(operatorIndex, 1, Integer::sum);
                        }
                    }
            } 
            operatorUsage.merge(operatorIndex, 1, Integer::sum);
            iterationSinceLastBest++;
            if (escape){
                escapeCount++;
            }
        }
        
    }
    
    /**
     * Update operator scores
     * @param operators
     * @param r - probability of accepting a worse solution
     * @param operatorUsage 
     * @param keepOperatorScores - current operator scores
     * @param i - run i 
     * @return
     */
    private WeightedRandomiser updateOperatorScores(List<OperatorScore> operators, double r,
            HashMap<Integer, Integer> operatorUsage, HashMap<Integer, Integer> keepOperatorScores, int i) {
        HashMap<Integer, Integer> operatorWeights = new HashMap<>();

        for (int j = 0; j < operators.size(); j++) {
            OperatorScore operatorScore = operators.get(j);
            int previousScore = operatorScore.getScore();
            int newScore = keepOperatorScores.getOrDefault(j, 1);
            int usage = operatorUsage.getOrDefault(j, 1);

            // Calculate new operator score
            double scaledUsage = Math.sqrt(usage);
            int currentScore = (int) Math.round(previousScore * (1 - r) + 20*(r * newScore / scaledUsage));
            currentScore = Math.max(currentScore, 1); //make sure score is at least 1
            operatorScore.setScore(currentScore);
            operatorWeights.put(j, currentScore);

            // Reset temporary scores only if the operator was used
            if (operatorUsage.containsKey(j) && operatorUsage.get(j) > 1) {
                keepOperatorScores.put(j, 1);
                operatorUsage.put(j, 1);
            }
        }

        WeightedRandomiser randomiser = new WeightedRandomiser(operatorWeights);
        return randomiser;
    }

    /**
     * Create operator combinations
     * Operator 0: expensive removal, greedy insertion
     * Operator 1: tightness removal, greedy insertion
     * Operator 2: random removal, greedy insertion
     * @return list of operator combinations
     */
    private List<OperatorScore> createOperatorCombinations(){
        List<OperatorScore> operators = new ArrayList<>();
        List<RemovalOperator> removalOperators = Arrays.asList(new RemovalOperator3(read),new RemovalOperator2(read), new RemovalOperator1(read));
        List<InsertOperator> insertOperators = Arrays.asList(new GreedyInsert(read));
        for (int i = 0; i < insertOperators.size(); i++){
            for (int j = 0; j < removalOperators.size(); j++){
                List<Operator> combinedOperator = new ArrayList<>();
                combinedOperator.add(removalOperators.get(j));
                combinedOperator.add(insertOperators.get(i));
                operators.add(new OperatorScore(combinedOperator, 16)); //approx 1/6 chance of being chosen
            }
        }
        return operators;
    }

    /**
     * Deep copy of solution
     * @param solution
     * @return
     */
    public static HashMap<Integer, List<Integer>> deepCopy(HashMap<Integer, List<Integer>> solution){
        return solution.entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new ArrayList<>(entry.getValue()),
                (a, b) -> a,
                HashMap::new));
    }


    public HashMap<Integer, List<Integer>> getBestSolution(){return bestSolution;}
    public int getObjectiveValue(){return bestObjectiveValue;}
}
