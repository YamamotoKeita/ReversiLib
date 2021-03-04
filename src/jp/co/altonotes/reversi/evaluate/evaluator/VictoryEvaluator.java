package jp.co.altonotes.reversi.evaluate.evaluator;

import jp.co.altonotes.reversi.model.Board;

/**
 * @author Yamamoto Keita
 *
 */
public class VictoryEvaluator extends Evaluator {
	int VICTORY_POINT = 1000;

	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.evaluate.evaluator.Evaluator#evaluate(jp.co.altonotes.reversi.model.Board)
	 */
	@Override
	public int evaluate(Board board) {
		if (board.isFull() || board.isNoFuture()) {
			int diff = board.getStoneCount(MY_STONE) - board.getStoneCount(ENEMY_STONE);
			if (0 < diff) {
				return VICTORY_POINT;
			}
			else if (diff < 0) {
				return -VICTORY_POINT;
			}
		}
		return 0;
	}

}
