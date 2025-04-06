package no.uib.inf101.sample.objects;

import java.util.List;

import no.uib.inf101.sample.operators.Operator;

public class OperatorScore {
    private List<Operator> operator;
    private int score;

    public OperatorScore(List<Operator> combinedOperator, int score){
        this.operator = combinedOperator;
        this.score = score;
    }

    public List<Operator> getOperator(){
        return operator;
    }
    
    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }
    
}
