package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

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
public class AiAgent extends Agent{
	
	
	public AiAgent() {
		this(PlayerTurn.PLAYER_ONE);
	}

	public long startTime = 0;
	
	public AiAgent(String name) {
		super(name, PlayerTurn.PLAYER_ONE);
	}
	
	public AiAgent(PlayerTurn playerTurn) {
		super(playerTurn);
	
	}

	/**
	 * Move method, Returns Ai Move
	 */
	@Override
	public AgentMove getMove(GameBoardState gameState) {

		startTime = AgentController.getElapsedTime(0);
		System.out.println("-------------------------------------");
		return getExampleMove(gameState, 0, new MoveAndValue(Integer.MIN_VALUE, null), new MoveAndValue(Integer.MAX_VALUE,null),true).move;
	}

	/**
	 * Minmax algoritm
	 * 
	 * @param gameState
	 * @return An ai move
	 */
	private MoveAndValue getExampleMove(GameBoardState gameState, int depth, MoveAndValue a, MoveAndValue b, boolean maximizingPlayer){
		//Creating debug string
		this.setNodesExamined(this.getNodesExamined()+1);
		String debugOffset = "";
		for(int i = 0; i< depth; i++){
			debugOffset+= "   ";
		}
		debugOffset += "|";

		//Maximizing players turn
		if(maximizingPlayer){
			List<ObjectiveWrapper> pathlist = AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_ONE);
			//Checks if the ai reached max depth, ran out of time or is at a terminal node
			if (depth == UserSettings.staticDepth || pathlist.size() == 0 || AgentController.timeLimitExceeded(UserSettings.MAX_SEARCH_TIME,startTime)) {
				if(this.getSearchDepth() < depth){
					this.setSearchDepth(depth);
				}
				this.setReachedLeafNodes(this.getReachedLeafNodes()+1);
				return new MoveAndValue(gameState.getWhiteCount()-gameState.getBlackCount(), new MoveWrapper(gameState.getLeadingMove()));
			}

			//debug
			System.out.println(debugOffset+" MAX");

			MoveAndValue value;
			MoveAndValue bestValue;

			//Creating default MoveAndValue
			try {
				bestValue = new MoveAndValue(Integer.MIN_VALUE, new MoveWrapper(pathlist.get(0)));
			}catch(Exception e){
				bestValue = new MoveAndValue(Integer.MIN_VALUE, null);
			}

			//debug
			System.out.println(debugOffset+" depth: "+depth);
			System.out.println(debugOffset+" Pathiist: "+pathlist.size());

			//looping through all possible paths for a node
			for(int i = 0; i < pathlist.size(); i++){

				//getting paths
				GameBoardState nextState = AgentController.getNewState(gameState,pathlist.get(i));

				//Recursive call one level deeper
				value = getExampleMove(nextState, depth+1, a, b, false);

				//Replaces current nodes value and move if the new one is better
				if(value.value > bestValue.value){
					bestValue = value;
					bestValue.move = new MoveWrapper(nextState.getLeadingMove());
				}

				//replaces a if the new value is better
				if(a.value < bestValue.value){
					System.out.println(debugOffset+bestValue.value+" Better than "+a.value);
					a = bestValue;
				}

				//debug
				System.out.println(debugOffset+" Value: "+value.value);

				//Prunes
				if(a.value >= b.value){
					this.setPrunedCounter(this.getPrunedCounter()+1);
					System.out.println(debugOffset+" Pruned");
					break;
				}
			}
			//debug
			System.out.println(debugOffset+" Returned");
			return bestValue;

		}else{
			List<ObjectiveWrapper> pathlist = AgentController.getAvailableMoves(gameState, PlayerTurn.PLAYER_TWO);

			//Checks if the ai reached max depth, ran out of time or is at a terminal node
			if (depth == UserSettings.staticDepth || pathlist.size() == 0 || AgentController.timeLimitExceeded(UserSettings.MAX_SEARCH_TIME,startTime)) {
				if(this.getSearchDepth() < depth){
					this.setSearchDepth(depth);
				}
				this.setReachedLeafNodes(this.getReachedLeafNodes()+1);
				return new MoveAndValue(gameState.getWhiteCount()-gameState.getBlackCount(), new MoveWrapper(gameState.getLeadingMove()));
			}
			//debug
			System.out.println(debugOffset+" MIN");


			MoveAndValue value;
			MoveAndValue bestValue;

			//Creating default MoveAndValue
			try {
				bestValue = new MoveAndValue(Integer.MAX_VALUE, new MoveWrapper(pathlist.get(0)));
			}catch(Exception e){
				bestValue = new MoveAndValue(Integer.MAX_VALUE, null);
			}

			//debug
			System.out.println(debugOffset+" depth: "+depth);
			System.out.println(debugOffset+" Pathiist: "+pathlist.size());

			//looping through all possible paths for a node
			for(int i = 0; i < pathlist.size(); i++){

				//getting paths
				GameBoardState nextState = AgentController.getNewState(gameState,pathlist.get(i));

				//Recursive call one level deeper
				value = getExampleMove(nextState, depth+1, a, b, true);

				//Replaces current nodes value and move if the new one is better
				if(value.value < bestValue.value){
					bestValue = value;
					bestValue.move = new MoveWrapper(nextState.getLeadingMove());
				}

				//replaces b if the new value is better
				if(b.value > bestValue.value){
					System.out.println(debugOffset+bestValue.value+" Better than "+b.value);
					b = bestValue;
				}
				//debug
				System.out.println(debugOffset+" Value: "+value.value);

				//prunes
				if(a.value >= b.value){
					this.setPrunedCounter(this.getPrunedCounter()+1);
					System.out.println(debugOffset+" Pruned");
					break;
				}
			}
			//debug
			System.out.println(debugOffset+" Returned");
			return bestValue;
		}
	}
}
