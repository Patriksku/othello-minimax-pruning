package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

import java.util.List;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 *
 * @author Eudy Contreras
 */
public class ABPruneAgent extends Agent{

    public ABPruneAgent() {
        super(PlayerTurn.PLAYER_ONE);
        // TODO Auto-generated constructor stub
    }

    public ABPruneAgent(PlayerTurn playerTurn) {
        super(playerTurn);
        // TODO Auto-generated constructor stub
    }

    /**
     * Delete the content of this method and Implement your logic here!
     */
    @Override
    public AgentMove getMove(GameBoardState gameState) {
        gameState.getBlackCount();
        AgentMove temp = getExampleMove(gameState, 0, new MoveAndValue(Integer.MIN_VALUE, null), new MoveAndValue(Integer.MAX_VALUE,null),true).move;
        int hej;

        return getExampleMove(gameState, 0, new MoveAndValue(Integer.MIN_VALUE, null), new MoveAndValue(Integer.MAX_VALUE,null),true).move;
    }

    /**
     * Default template move which serves as an example of how to implement move
     * making logic. Note that this method does not use Alpha beta pruning and
     * the use of this method can disqualify you
     *
     * @param gameState
     * @return
     */
    private MoveAndValue getExampleMove(GameBoardState gameState, int depth, MoveAndValue a, MoveAndValue b, boolean maximizingPlayer){

        if(depth > 9) {
            return new MoveAndValue(gameState.getBlackCount()-gameState.getWhiteCount(), new MoveWrapper(gameState.getLeadingMove()));
        }
        if(maximizingPlayer){
            MoveAndValue value = null;
            for(int i = 0; i < gameState.getChildStates().size(); i++){
                 value = getExampleMove(gameState.getChildStates().get(i), depth+1, a, b, false);
                 if(a.value < value.value){
                    a = value;
                 }
                if(a.value >= b.value){
                    break;
                }
            }
            if(gameState.isTerminal()) {
                return new MoveAndValue(gameState.getBlackCount()-gameState.getWhiteCount(), new MoveWrapper(gameState.getLeadingMove()));
            }else {
                return value;
            }
        }else{
            MoveAndValue value = null;
            for(int i = 0; i < gameState.getChildStates().size(); i++){
                value = getExampleMove(gameState.getChildStates().get(i), depth+1, a, b, false);
                if(a.value > value.value){
                    a = value;
                }
                if(a.value >= b.value){
                    break;
                }
            }
            return value;
        }
    }



}
