package main;

import com.eudycontreras.othello.capsules.AgentMove;

public class MoveAndValue {
    public int value;
    public AgentMove move;


    public MoveAndValue(int value, AgentMove move){
        this.move = move;
        this.value = value;
    }
}
