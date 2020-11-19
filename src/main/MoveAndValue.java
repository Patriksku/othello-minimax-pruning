package main;

import com.eudycontreras.othello.capsules.AgentMove;

public class MoveAndValue {
    public double value;
    public AgentMove move;


    public MoveAndValue(double value, AgentMove move){
        this.move = move;
        this.value = value;
    }
}
