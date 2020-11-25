package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
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
public class ExampleAgentTwo extends Agent{


	public ExampleAgentTwo() {
		this(PlayerTurn.PLAYER_ONE);
	}

	public long startTime = 0;

	public ExampleAgentTwo(String name) {
		super(name, PlayerTurn.PLAYER_ONE);
	}

	public ExampleAgentTwo(PlayerTurn playerTurn) {
		super(playerTurn);

	}

	/**
	 * Delete the content of this method and Implement your logic here!
	 */
	@Override
	public AgentMove getMove(GameBoardState gameState) {

		startTime = AgentController.getElapsedTime(0);
		System.out.println("-------------------------------------");
		return getExampleMove(gameState, 0, new MoveAndValue(Integer.MIN_VALUE, null), new MoveAndValue(Integer.MAX_VALUE,null),false).move;
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
		this.setNodesExamined(this.getNodesExamined()+1);
		String debugOffset = "";
		for(int i = 0; i< depth; i++){
			debugOffset+= "   ";
		}
		debugOffset += "|";
		if(maximizingPlayer){
			if (depth == UserSettings.staticDepth2 || AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_ONE).size() == 0 || AgentController.timeLimitExceeded(UserSettings.MAX_SEARCH_TIME,startTime)) {
				if(this.getSearchDepth() < depth){
					this.setSearchDepth(depth);
				}
				this.setReachedLeafNodes(this.getReachedLeafNodes()+1);
				return new MoveAndValue((int)AgentController.getMobilityHeuristic(gameState), new MoveWrapper(gameState.getLeadingMove()));
			}
			System.out.println(debugOffset+" MAX");
			List<ObjectiveWrapper> pathlist = AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_ONE);
			MoveAndValue value;
			MoveAndValue bestValue;
			try {
				bestValue = new MoveAndValue(Integer.MIN_VALUE, new MoveWrapper(pathlist.get(0)));
			}catch(Exception e){
				bestValue = new MoveAndValue(Integer.MIN_VALUE, null);
			}
			System.out.println(debugOffset+" depth: "+depth);
			System.out.println(debugOffset+" Pathiist: "+pathlist.size());
			for(int i = 0; i < pathlist.size(); i++){

				GameBoardState nextState = AgentController.getNewState(gameState,pathlist.get(i));

				value = getExampleMove(nextState, depth+1, a, b, false);

				if(value.value > bestValue.value){
					bestValue = value;
					bestValue.move = new MoveWrapper(nextState.getLeadingMove());
				}
				if(a.value < bestValue.value){
					System.out.println(debugOffset+bestValue.value+" Better than "+a.value);
					a = bestValue;
				}
				System.out.println(debugOffset+" Value: "+value.value);
				if(a.value >= b.value){
					this.setPrunedCounter(this.getPrunedCounter()+1);
					System.out.println(debugOffset+" Pruned");
					break;
				}

			}
			System.out.println(debugOffset+" Returned");
			return bestValue;

		}else{
			if (depth == UserSettings.staticDepth2 || AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_TWO).size() == 0 || AgentController.timeLimitExceeded(UserSettings.MAX_SEARCH_TIME,startTime)) {
				if(this.getSearchDepth() < depth){
					this.setSearchDepth(depth);
				}
				this.setReachedLeafNodes(this.getReachedLeafNodes()+1);
				return new MoveAndValue((int)AgentController.getMobilityHeuristic(gameState), new MoveWrapper(gameState.getLeadingMove()));
			}
			System.out.println(debugOffset+" MIN");
			List<ObjectiveWrapper> pathlist = AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_TWO);
			MoveAndValue value;
			MoveAndValue bestValue;
			try {
				bestValue = new MoveAndValue(Integer.MAX_VALUE, new MoveWrapper(pathlist.get(0)));
			}catch(Exception e){
				bestValue = new MoveAndValue(Integer.MAX_VALUE, null);
			}
			System.out.println(debugOffset+" depth: "+depth);
			System.out.println(debugOffset+" Pathiist: "+pathlist.size());
			for(int i = 0; i < pathlist.size(); i++){

				GameBoardState nextState = AgentController.getNewState(gameState,pathlist.get(i));

				value = getExampleMove(nextState, depth+1, a, b, true);

				if(value.value < bestValue.value){
					bestValue = value;
					bestValue.move = new MoveWrapper(nextState.getLeadingMove());
				}
				if(b.value > bestValue.value){
					System.out.println(debugOffset+bestValue.value+" Better than "+b.value);
					b = bestValue;
				}

				System.out.println(debugOffset+" Value: "+value.value);
				if(a.value >= b.value){
					this.setPrunedCounter(this.getPrunedCounter()+1);
					System.out.println(debugOffset+" Pruned");
					break;
				}
			}
			System.out.println(debugOffset+" Returned");
			return bestValue;
		}
	}

}
