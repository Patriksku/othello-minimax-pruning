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

import java.math.BigDecimal;
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
public class ExampleAgentOne extends Agent{

	private int depthLimit = 8;


	public ExampleAgentOne() {
		this(PlayerTurn.PLAYER_ONE);
	}

	public ExampleAgentOne(String name) {
		super(name, PlayerTurn.PLAYER_ONE);
	}

	public ExampleAgentOne(PlayerTurn playerTurn) {
		super(playerTurn);

	}

	/**
	 * Delete the content of this method and Implement your logic here!
	 */
	@Override
	public AgentMove getMove(GameBoardState gameState) {
		return getExampleMove(gameState, 0, Double.MIN_VALUE, Double.MAX_VALUE,true).move;
	}

	/**
	 * Default template move which serves as an example of how to implement move
	 * making logic. Note that this method does not use Alpha beta pruning and
	 * the use of this method can disqualify you
	 *
	 * @param gameState
	 * @return
	 */
	private MoveAndValue getExampleMove(GameBoardState gameState, int depth, double alpha, double beta, boolean maximizingPlayer){


		if (depth == depthLimit || AgentController.isTerminal(gameState, playerTurn)) {
			double value = AgentController.getMobilityHeuristic(gameState);
			return new MoveAndValue(value, new MoveWrapper(gameState.getLeadingMove()));
		}


		if (maximizingPlayer) {
			MoveAndValue maxEval = new MoveAndValue(Double.MIN_VALUE, null);

			List<ObjectiveWrapper> availableMoves = AgentController.getAvailableMoves(gameState, playerTurn);

			for (ObjectiveWrapper move : availableMoves) {
				GameBoardState child = AgentController.getNewState(gameState, move); // Create child (a game board state)

				MoveAndValue eval = getExampleMove(child, (depth + 1), alpha, beta, false);
				maxEval.value = Math.max(maxEval.value, eval.value);

				// Choose the first available move if no move has been assigned as the best yet.
				if (maxEval.move == null) {
					maxEval.move = new MoveWrapper(child.getLeadingMove());
				} else if (maxEval.value > eval.value) { // Compare current move with the newly found move, if better --> Replace.
					maxEval.move = eval.move;
				}

				alpha = Math.max(alpha, eval.value);
				System.out.println("alpha: " + alpha);

				if (beta <= alpha) {
					System.out.println("PRUNED");
					break;
				}
			}

			return maxEval;

		} else {
			MoveAndValue minEval = new MoveAndValue(Double.MAX_VALUE, null);

			List<ObjectiveWrapper> availableMoves = AgentController.getAvailableMoves(gameState, playerTurn);

			for (ObjectiveWrapper move : availableMoves) {
				GameBoardState child = AgentController.getNewState(gameState, move); // Create child (a game board state)

				MoveAndValue eval = getExampleMove(child, (depth + 1), alpha, beta, true);
				minEval.value = Math.min(minEval.value, eval.value);

				// Choose the first available move if no move has been assigned as the best yet.
				if (minEval.move == null) {
					minEval.move = new MoveWrapper(child.getLeadingMove());
				} else if (minEval.value < eval.value) { // Compare current move with the newly found move, if better --> Replace.
					minEval.move = eval.move;
				}

				beta = Math.min(beta, eval.value);
				System.out.println("beta: " + beta);

				if (beta <= alpha) {
					System.out.println("PRUNED");
					break;
				}
			}

			return minEval;
		}
	}
}
