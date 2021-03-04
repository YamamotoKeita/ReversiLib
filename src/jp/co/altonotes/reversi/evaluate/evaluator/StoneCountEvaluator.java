package jp.co.altonotes.reversi.evaluate.evaluator;

import java.util.ArrayList;

import jp.co.altonotes.reversi.model.Board;

/**
 * @author Yamamoto Keita
 *
 */
public class StoneCountEvaluator extends Evaluator {

	private ArrayList<int[]> adjustLine = new ArrayList<int[]>();
	
	public StoneCountEvaluator() {
		adjustLine.add(new int[]{40, 0});
		adjustLine.add(new int[]{50, 1});
		adjustLine.add(new int[]{55, 4});
		adjustLine.add(new int[]{60, 8});
		adjustLine.add(new int[]{63, 15});
		adjustLine.add(new int[]{64, 20});
	}
	
	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.evaluate.evaluator.Evaluator#evaluate(jp.co.altonotes.reversi.model.Board)
	 */
	@Override
	public int evaluate(Board board) {
		int value = 0;
		
		int stoneCount = board.getStoneCount();
		
		int myCount = board.getStoneCount(MY_STONE);
		int oppositeCount = board.getStoneCount(ENEMY_STONE);
		
		value = myCount - oppositeCount;

		if (board.isNoFuture()) {
			value *= adjustLine.get(adjustLine.size() - 1)[1];
		} else {
			for (int[] point : adjustLine) {
				if (stoneCount <= point[0]) {
					value *= point[1];
					break;
				}
			}
		}

		return value;
	}

}
